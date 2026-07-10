package pe.upc.pescagobackend.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pe.upc.pescagobackend.hiredService.domain.model.aggregates.HiredService;
import pe.upc.pescagobackend.hiredService.infrastructure.persistence.jpa.repositories.HiredServiceRepository;
import pe.upc.pescagobackend.receipt.infrastructure.persistence.jpa.repositories.ReceiptRepository;
import pe.upc.pescagobackend.request.application.internal.commandservices.RequestPaymentService;
import pe.upc.pescagobackend.request.domain.model.aggregates.Dimensions;
import pe.upc.pescagobackend.request.domain.model.aggregates.Request;
import pe.upc.pescagobackend.request.infrastructure.persistence.jpa.repositories.RequestRepository;
import pe.upc.pescagobackend.request.interfaces.rest.resources.PayRequestResource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class RequestPaymentRollbackIntegrationTest {

    @Autowired
    private RequestPaymentService requestPaymentService;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @MockBean
    private HiredServiceRepository hiredServiceRepository;

    @Test
    void whenHiredServiceSaveFailsNoPartialPaymentStateRemains() {
        when(hiredServiceRepository.save(any(HiredService.class)))
                .thenThrow(new RuntimeException("forced hired-service failure"));

        Request request = saveQuotedRequest();
        long receiptsBefore = receiptRepository.count();

        assertThatThrownBy(() -> requestPaymentService.pay(
                request.getId(),
                new PayRequestResource(
                        "Juan Perez",
                        "4111111111111111",
                        "2028-06",
                        "123",
                        "2026-07-10T12:00:00Z",
                        "visa"
                )
        )).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("forced hired-service failure");

        Request reloaded = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(reloaded.getStatus()).isEqualTo("QUOTED");
        assertThat(receiptRepository.count()).isEqualTo(receiptsBefore);
    }

    private Request saveQuotedRequest() {
        Request request = new Request();
        request.setEntrepreneurId(4L);
        request.setEntrepreneurName("Rollback Entrepreneur");
        request.setCarrierId(5L);
        request.setCarrierName("Rollback Carrier");
        request.setPackageDescription("Rollback package");
        request.setQuantity(1);
        request.setWeightTotal(5.0);
        request.setPickupLocation("Lima");
        request.setDeliveryLocation("Callao");
        request.setPickupDateTime(LocalDateTime.parse("2026-07-20T10:00:00"));
        request.setPrice(120.0);
        request.setStatus("QUOTED");

        Dimensions dimensions = new Dimensions();
        dimensions.setLength(1.0);
        dimensions.setWidth(1.0);
        dimensions.setHeight(1.0);
        request.setDimensions(dimensions);

        return requestRepository.save(request);
    }
}
