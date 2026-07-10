package pe.upc.pescagobackend.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.upc.pescagobackend.hiredService.infrastructure.persistence.jpa.repositories.HiredServiceRepository;
import pe.upc.pescagobackend.receipt.domain.model.ReceiptSensitiveDataSanitizer;
import pe.upc.pescagobackend.receipt.infrastructure.persistence.jpa.repositories.ReceiptRepository;
import pe.upc.pescagobackend.request.domain.model.aggregates.Dimensions;
import pe.upc.pescagobackend.request.domain.model.aggregates.Request;
import pe.upc.pescagobackend.request.infrastructure.persistence.jpa.repositories.RequestRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RequestPaymentV1IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private HiredServiceRepository hiredServiceRepository;

    @Test
    void payQuotedRequestCreatesSanitizedReceiptPaidRequestAndPendingHiredService() throws Exception {
        var request = saveQuotedRequest(150.0);

        mockMvc.perform(post("/api/v1/requests/{id}/pay", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "holderName": "Juan Perez",
                                  "cardNumber": "4111111111111111",
                                  "expiryDate": "2028-06",
                                  "cvv": "123",
                                  "paymentDate": "2026-07-10T12:00:00Z",
                                  "paymentMethod": "visa"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requestId").value(request.getId()))
                .andExpect(jsonPath("$.receiptId").isNumber())
                .andExpect(jsonPath("$.hiredServiceId").isNumber())
                .andExpect(jsonPath("$.status").value("Pagado"))
                .andExpect(jsonPath("$.paymentMethod").value("visa"));

        var updated = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo("PAID");

        var receipt = receiptRepository.findAll().stream()
                .filter(item -> request.getId().equals(item.getRequestId()))
                .findFirst()
                .orElseThrow();
        assertThat(receipt.getCardNumber()).isEqualTo("CARD-****-1111");
        assertThat(receipt.getCvv()).isEqualTo(ReceiptSensitiveDataSanitizer.CVV_NOT_STORED);

        var hired = hiredServiceRepository.findAll().stream()
                .filter(item -> request.getId().equals(item.getRequestId()))
                .findFirst()
                .orElseThrow();
        assertThat(hired.getStatus()).isEqualTo("PENDING_CONFIRMATION");
        assertThat(hired.getPaymentMethod()).isEqualTo("visa");
        assertThat(hired.getCarrierData().getDriver()).isEmpty();
        assertThat(hired.getCarrierData().getPlate()).isEmpty();
        assertThat(hired.getCarrierData().getVehicleBrand()).isEmpty();
    }

    @Test
    void payWalletYapePreservesWalletTokenAndNotStoredCvv() throws Exception {
        var request = saveQuotedRequest(80.0);

        mockMvc.perform(post("/api/v1/requests/{id}/pay", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "holderName": "Pago simulado Yape",
                                  "cardNumber": "WALLET-YAPE",
                                  "expiryDate": "2026-07",
                                  "cvv": "NOT_STORED",
                                  "paymentMethod": "yape"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentMethod").value("yape"))
                .andExpect(jsonPath("$.status").value("Pagado"));

        var receipt = receiptRepository.findAll().stream()
                .filter(item -> request.getId().equals(item.getRequestId()))
                .findFirst()
                .orElseThrow();
        assertThat(receipt.getCardNumber()).isEqualTo("WALLET-YAPE");
        assertThat(receipt.getCvv()).isEqualTo(ReceiptSensitiveDataSanitizer.CVV_NOT_STORED);
    }

    @Test
    void payWalletPlinPreservesWalletToken() throws Exception {
        var request = saveQuotedRequest(90.0);

        mockMvc.perform(post("/api/v1/requests/{id}/pay", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "holderName": "Pago simulado Plin",
                                  "cardNumber": "WALLET-PLIN",
                                  "expiryDate": "2026-07",
                                  "cvv": "999",
                                  "paymentMethod": "plin"
                                }
                                """))
                .andExpect(status().isCreated());

        var receipt = receiptRepository.findAll().stream()
                .filter(item -> request.getId().equals(item.getRequestId()))
                .findFirst()
                .orElseThrow();
        assertThat(receipt.getCardNumber()).isEqualTo("WALLET-PLIN");
        assertThat(receipt.getCvv()).isEqualTo(ReceiptSensitiveDataSanitizer.CVV_NOT_STORED);
    }

    @Test
    void payRejectsWhenRequestIsNotQuoted() throws Exception {
        var request = saveRequest("PENDING", 150.0);
        long receiptsBefore = receiptRepository.count();
        long hiredBefore = hiredServiceRepository.count();

        mockMvc.perform(post("/api/v1/requests/{id}/pay", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPayJson("visa")))
                .andExpect(status().isBadRequest());

        var unchanged = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(unchanged.getStatus()).isEqualTo("PENDING");
        assertThat(receiptRepository.count()).isEqualTo(receiptsBefore);
        assertThat(hiredServiceRepository.count()).isEqualTo(hiredBefore);
    }

    @Test
    void payRejectsWhenQuotedPriceIsMissing() throws Exception {
        var request = saveRequest("QUOTED", 0.0);
        long receiptsBefore = receiptRepository.count();

        mockMvc.perform(post("/api/v1/requests/{id}/pay", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPayJson("mastercard")))
                .andExpect(status().isBadRequest());

        assertThat(requestRepository.findById(request.getId()).orElseThrow().getStatus()).isEqualTo("QUOTED");
        assertThat(receiptRepository.count()).isEqualTo(receiptsBefore);
    }

    @Test
    void payReturns404WhenRequestDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/v1/requests/{id}/pay", 999999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPayJson("visa")))
                .andExpect(status().isNotFound());
    }

    private Request saveQuotedRequest(double price) {
        return saveRequest("QUOTED", price);
    }

    private Request saveRequest(String status, double price) {
        Request request = new Request();
        request.setEntrepreneurId(2L);
        request.setEntrepreneurName("Entrepreneur Pay");
        request.setCarrierId(3L);
        request.setCarrierName("Carrier Pay");
        request.setPackageDescription("Atomic payment package");
        request.setQuantity(2);
        request.setWeightTotal(20.0);
        request.setPickupLocation("Lima");
        request.setDeliveryLocation("Callao");
        request.setPickupDateTime(LocalDateTime.parse("2026-07-15T10:00:00"));
        request.setPrice(price);
        request.setStatus(status);

        Dimensions dimensions = new Dimensions();
        dimensions.setLength(1.0);
        dimensions.setWidth(1.0);
        dimensions.setHeight(1.0);
        request.setDimensions(dimensions);

        return requestRepository.save(request);
    }

    private String validPayJson(String paymentMethod) {
        return """
                {
                  "holderName": "Juan Perez",
                  "cardNumber": "4111111111111111",
                  "expiryDate": "2028-06",
                  "cvv": "123",
                  "paymentDate": "2026-07-10T12:00:00Z",
                  "paymentMethod": "%s"
                }
                """.formatted(paymentMethod);
    }
}
