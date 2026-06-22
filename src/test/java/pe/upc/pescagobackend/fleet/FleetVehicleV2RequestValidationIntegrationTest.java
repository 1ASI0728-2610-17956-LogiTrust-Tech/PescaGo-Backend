package pe.upc.pescagobackend.fleet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import pe.upc.pescagobackend.carrier.domain.model.aggregates.Carrier;
import pe.upc.pescagobackend.carrier.infrastructure.persistence.jpa.repositories.CarrierRepository;
import pe.upc.pescagobackend.fleet.domain.model.aggregates.Vehicle;
import pe.upc.pescagobackend.fleet.domain.model.commands.RegisterVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleAvailabilityStatus;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleType;
import pe.upc.pescagobackend.fleet.infrastructure.persistence.jpa.repositories.VehicleRepository;
import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FleetVehicleV2RequestValidationIntegrationTest {

    private static final String VEHICLES_URL = "/api/v2/fleet/vehicles";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarrierRepository carrierRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String carrierToken;
    private Long carrierId;

    @BeforeEach
    void setUpCarrierSession() throws Exception {
        String email = "validation-" + UUID.randomUUID() + "@example.com";
        var carrierUser = saveUser(email, "secret123", "carrier");
        carrierId = saveCarrier(carrierUser.getId(), "Validation Carrier").getId();
        carrierToken = loginAndGetToken(email, "secret123");
    }

    @Test
    void postWithInvalidVehicleTypeReturns400() throws Exception {
        mockMvc.perform(post(VEHICLES_URL)
                        .header("Authorization", "Bearer " + carrierToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "plate": "VAL-001",
                                  "vehicleType": "NOT_A_TYPE",
                                  "maxWeightKg": 1500.00,
                                  "refrigerated": true
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postWithInvalidAvailabilityStatusReturns400() throws Exception {
        mockMvc.perform(post(VEHICLES_URL)
                        .header("Authorization", "Bearer " + carrierToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "plate": "VAL-002",
                                  "vehicleType": "TRUCK",
                                  "maxWeightKg": 1500.00,
                                  "refrigerated": true,
                                  "availabilityStatus": "NOT_A_STATUS"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postWithoutRefrigeratedReturns400() throws Exception {
        mockMvc.perform(post(VEHICLES_URL)
                        .header("Authorization", "Bearer " + carrierToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "plate": "VAL-003",
                                  "vehicleType": "TRUCK",
                                  "maxWeightKg": 1500.00
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putWithoutRefrigeratedReturns400() throws Exception {
        var vehicle = saveVehicle(carrierId, "VAL-004", true);

        mockMvc.perform(put(VEHICLES_URL + "/{id}", vehicle.getId())
                        .header("Authorization", "Bearer " + carrierToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "plate": "VAL-004",
                                  "vehicleType": "TRUCK",
                                  "maxWeightKg": 1200.00
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putWithoutAvailabilityStatusReturns400() throws Exception {
        var vehicle = saveVehicle(carrierId, "VAL-005", true);

        mockMvc.perform(put(VEHICLES_URL + "/{id}", vehicle.getId())
                        .header("Authorization", "Bearer " + carrierToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "plate": "VAL-005",
                                  "vehicleType": "TRUCK",
                                  "maxWeightKg": 1200.00,
                                  "refrigerated": true
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postWithValidPayloadReturns201() throws Exception {
        mockMvc.perform(post(VEHICLES_URL)
                        .header("Authorization", "Bearer " + carrierToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "plate": "VAL-006",
                                  "vehicleType": "TRUCK",
                                  "maxWeightKg": 1500.00,
                                  "refrigerated": false
                                }
                                """))
                .andExpect(status().isCreated());
    }

    private String loginAndGetToken(String email, String password) throws Exception {
        var response = mockMvc.perform(post("/api/v2/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"%s"}
                                """.formatted(email, password)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return new com.fasterxml.jackson.databind.ObjectMapper().readTree(response).get("accessToken").asText();
    }

    private User saveUser(String email, String plainPassword, String role) {
        User user = new User();
        user.setUsername(email);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(plainPassword));
        user.setRole(role);
        return userRepository.save(user);
    }

    private Carrier saveCarrier(Long userId, String name) {
        Carrier carrier = new Carrier();
        carrier.setUserId(userId);
        carrier.setName(name);
        carrier.setDescription("Test carrier");
        return carrierRepository.save(carrier);
    }

    private Vehicle saveVehicle(Long carrierId, String plate, boolean active) {
        var vehicle = new Vehicle(new RegisterVehicleCommand(
                carrierId,
                plate,
                VehicleType.TRUCK,
                new BigDecimal("1000.00"),
                null,
                false,
                VehicleAvailabilityStatus.AVAILABLE
        ));
        vehicle.setActive(active);
        return vehicleRepository.save(vehicle);
    }
}
