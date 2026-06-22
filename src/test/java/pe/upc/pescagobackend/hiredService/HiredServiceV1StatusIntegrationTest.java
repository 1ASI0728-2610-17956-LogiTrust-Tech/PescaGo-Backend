package pe.upc.pescagobackend.hiredService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.upc.pescagobackend.hiredService.domain.model.aggregates.CarrierData;
import pe.upc.pescagobackend.hiredService.domain.model.aggregates.HiredService;
import pe.upc.pescagobackend.hiredService.infrastructure.persistence.jpa.repositories.HiredServiceRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HiredServiceV1StatusIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HiredServiceRepository hiredServiceRepository;

    @Test
    void postWithPendientePersistsPendingConfirmation() throws Exception {
        mockMvc.perform(post("/api/v1/hired-services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreateHiredServiceJson("Pendiente", "Fish package post pendiente")))
                .andExpect(status().isOk());

        var saved = hiredServiceRepository.findAll().stream()
                .filter(service -> "Fish package post pendiente".equals(service.getPackageDescription()))
                .findFirst()
                .orElseThrow();

        assertThat(saved.getStatus()).isEqualTo("PENDING_CONFIRMATION");
    }

    @Test
    void getWithPendingConfirmationReturnsPendiente() throws Exception {
        var service = saveHiredServiceWithStatus("PENDING_CONFIRMATION");

        mockMvc.perform(get("/api/v1/hired-services/{id}", service.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Pendiente"));
    }

    @Test
    void putWithConfirmadoPersistsConfirmed() throws Exception {
        var service = saveHiredServiceWithStatus("PENDING_CONFIRMATION");

        mockMvc.perform(put("/api/v1/hired-services/{id}", service.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdateHiredServiceJson(service, "Confirmado")))
                .andExpect(status().isOk());

        var updated = hiredServiceRepository.findById(service.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo("CONFIRMED");
    }

    @Test
    void putWithCotizadoReturns400() throws Exception {
        var service = saveHiredServiceWithStatus("PENDING_CONFIRMATION");

        mockMvc.perform(put("/api/v1/hired-services/{id}", service.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdateHiredServiceJson(service, "Cotizado")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void legacyPendingRowReturnsPendiente() throws Exception {
        var service = saveHiredServiceWithStatus("PENDING");

        mockMvc.perform(get("/api/v1/hired-services/{id}", service.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Pendiente"));
    }

    private HiredService saveHiredServiceWithStatus(String status) {
        HiredService service = new HiredService();
        service.setRequestId(1L);
        service.setEntrepreneurId(1L);
        service.setEntrepreneurName("Entrepreneur One");
        service.setCarrierId(1L);
        service.setCarrierName("Carrier One");
        service.setPackageDescription("Fish package");
        service.setPickupDateTime(LocalDateTime.parse("2026-06-21T10:00:00"));
        service.setPaymentMethod("CARD");
        service.setStatus(status);

        CarrierData carrierData = new CarrierData();
        carrierData.setVehicleBrand("Toyota");
        carrierData.setPlate("ABC-123");
        carrierData.setDriver("Driver One");
        service.setCarrierData(carrierData);

        return hiredServiceRepository.save(service);
    }

    private String validCreateHiredServiceJson(String status, String packageDescription) {
        return """
                {
                  "requestId": 1,
                  "entrepreneurId": 1,
                  "entrepreneurName": "Entrepreneur One",
                  "carrierId": 1,
                  "carrierName": "Carrier One",
                  "packageDescription": "%s",
                  "pickupDateTime": "2026-06-21T10:00:00",
                  "paymentMethod": "CARD",
                  "status": "%s",
                  "carrierData": {
                    "vehicleBrand": "Toyota",
                    "plate": "ABC-123",
                    "driver": "Driver One"
                  }
                }
                """.formatted(packageDescription, status);
    }

    private String validUpdateHiredServiceJson(HiredService service, String status) {
        return """
                {
                  "requestId": %d,
                  "entrepreneurId": %d,
                  "entrepreneurName": "%s",
                  "carrierId": %d,
                  "carrierName": "%s",
                  "packageDescription": "%s",
                  "pickupDateTime": "%s",
                  "paymentMethod": "%s",
                  "status": "%s",
                  "carrierData": {
                    "vehicleBrand": "Toyota",
                    "plate": "ABC-123",
                    "driver": "Driver One"
                  }
                }
                """.formatted(
                service.getRequestId(),
                service.getEntrepreneurId(),
                service.getEntrepreneurName(),
                service.getCarrierId(),
                service.getCarrierName(),
                service.getPackageDescription(),
                service.getPickupDateTime(),
                service.getPaymentMethod(),
                status
        );
    }
}
