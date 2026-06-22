package pe.upc.pescagobackend.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.upc.pescagobackend.request.domain.model.aggregates.Dimensions;
import pe.upc.pescagobackend.request.domain.model.aggregates.Request;
import pe.upc.pescagobackend.request.infrastructure.persistence.jpa.repositories.RequestRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RequestV1StatusIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    void postWithPendientePersistsPending() throws Exception {
        mockMvc.perform(post("/api/v1/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreateRequestJson("Pendiente", "Fish package post pendiente")))
                .andExpect(status().isOk());

        var saved = requestRepository.findAll().stream()
                .filter(request -> "Fish package post pendiente".equals(request.getPackageDescription()))
                .findFirst()
                .orElseThrow();

        assertThat(saved.getStatus()).isEqualTo("PENDING");
    }

    @Test
    void getRequestWithPendingReturnsPendiente() throws Exception {
        var request = saveRequestWithStatus("PENDING");

        mockMvc.perform(get("/api/v1/requests/{id}", request.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Pendiente"));
    }

    @Test
    void putWithCotizadoPersistsQuoted() throws Exception {
        var request = saveRequestWithStatus("PENDING");

        mockMvc.perform(put("/api/v1/requests/{id}", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdateRequestJson(request, "Cotizado", 150.0)))
                .andExpect(status().isOk());

        var updated = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo("QUOTED");
    }

    @Test
    void putWithPagadoPersistsPaid() throws Exception {
        var request = saveRequestWithStatus("QUOTED");

        mockMvc.perform(put("/api/v1/requests/{id}", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdateRequestJson(request, "Pagado", 150.0)))
                .andExpect(status().isOk());

        var updated = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo("PAID");
    }

    @Test
    void putWithConfirmadoReturns400() throws Exception {
        var request = saveRequestWithStatus("QUOTED");

        mockMvc.perform(put("/api/v1/requests/{id}", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdateRequestJson(request, "Confirmado", 150.0)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void legacySpanishRowStillReturnsPendiente() throws Exception {
        var request = saveRequestWithStatus("Pendiente");

        mockMvc.perform(get("/api/v1/requests/{id}", request.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Pendiente"));
    }

    private Request saveRequestWithStatus(String status) {
        Request request = new Request();
        request.setEntrepreneurId(1L);
        request.setEntrepreneurName("Entrepreneur One");
        request.setCarrierId(1L);
        request.setCarrierName("Carrier One");
        request.setPackageDescription("Fish package");
        request.setQuantity(1);
        request.setWeightTotal(10.0);
        request.setPickupLocation("Lima");
        request.setDeliveryLocation("Callao");
        request.setPickupDateTime(LocalDateTime.parse("2026-06-21T10:00:00"));
        request.setPrice(0.0);
        request.setStatus(status);

        Dimensions dimensions = new Dimensions();
        dimensions.setLength(1.0);
        dimensions.setWidth(1.0);
        dimensions.setHeight(1.0);
        request.setDimensions(dimensions);

        return requestRepository.save(request);
    }

    private String validCreateRequestJson(String status, String packageDescription) {
        return """
                {
                  "entrepreneurId": 1,
                  "entrepreneurName": "Entrepreneur One",
                  "carrierId": 1,
                  "carrierName": "Carrier One",
                  "packageDescription": "%s",
                  "quantity": 1,
                  "weightTotal": 10.0,
                  "pickupLocation": "Lima",
                  "deliveryLocation": "Callao",
                  "pickupDateTime": "2026-06-21T10:00:00",
                  "price": 0,
                  "status": "%s",
                  "dimensions": {
                    "length": 1.0,
                    "width": 1.0,
                    "height": 1.0
                  }
                }
                """.formatted(packageDescription, status);
    }

    private String validUpdateRequestJson(Request request, String status, double price) {
        return """
                {
                  "entrepreneurId": %d,
                  "entrepreneurName": "%s",
                  "carrierId": %d,
                  "carrierName": "%s",
                  "packageDescription": "%s",
                  "quantity": %d,
                  "weightTotal": %s,
                  "pickupLocation": "%s",
                  "deliveryLocation": "%s",
                  "pickupDateTime": "%s",
                  "price": %s,
                  "status": "%s",
                  "dimensions": {
                    "length": 1.0,
                    "width": 1.0,
                    "height": 1.0
                  }
                }
                """.formatted(
                request.getEntrepreneurId(),
                request.getEntrepreneurName(),
                request.getCarrierId(),
                request.getCarrierName(),
                request.getPackageDescription(),
                request.getQuantity(),
                request.getWeightTotal(),
                request.getPickupLocation(),
                request.getDeliveryLocation(),
                request.getPickupDateTime(),
                price,
                status
        );
    }
}
