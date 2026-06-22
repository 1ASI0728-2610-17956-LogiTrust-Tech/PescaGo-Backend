package pe.upc.pescagobackend.fleet;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FleetVehicleV2IntegrationTest {

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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postWithoutJwtReturns401() throws Exception {
        mockMvc.perform(post(VEHICLES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRegisterPayload("AAA-111")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void postAsEntrepreneurReturns403() throws Exception {
        saveUser("entrepreneur-fleet@example.com", "secret123", "entrepreneur");
        var token = loginAndGetToken("entrepreneur-fleet@example.com", "secret123");

        mockMvc.perform(post(VEHICLES_URL)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRegisterPayload("BBB-222")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.title").value("Forbidden"));
    }

    @Test
    void postAsCarrierWithoutCarrierProfileReturns404() throws Exception {
        saveUser("orphan-carrier@example.com", "secret123", "carrier");
        var token = loginAndGetToken("orphan-carrier@example.com", "secret123");

        mockMvc.perform(post(VEHICLES_URL)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRegisterPayload("CCC-333")))
                .andExpect(status().isNotFound());
    }

    @Test
    void postValidAsCarrierReturns201() throws Exception {
        var carrierUser = saveUser("carrier-fleet@example.com", "secret123", "carrier");
        saveCarrier(carrierUser.getId(), "Fleet Carrier");
        var token = loginAndGetToken("carrier-fleet@example.com", "secret123");

        mockMvc.perform(post(VEHICLES_URL)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRegisterPayload(" ddd-444 ")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.plate").value("DDD-444"))
                .andExpect(jsonPath("$.vehicleType").value("TRUCK"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.availabilityStatus").value("AVAILABLE"))
                .andExpect(jsonPath("$.carrierId").doesNotExist());
    }

    @Test
    void persistedCarrierIdComesFromJwtNotRequest() throws Exception {
        var carrierUser = saveUser("carrier-source@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Source Carrier");
        var token = loginAndGetToken("carrier-source@example.com", "secret123");

        var response = mockMvc.perform(post(VEHICLES_URL)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRegisterPayload("EEE-555")))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long vehicleId = objectMapper.readTree(response).get("id").asLong();
        var persisted = vehicleRepository.findById(vehicleId).orElseThrow();
        assertThat(persisted.getCarrierId()).isEqualTo(carrier.getId());
    }

    @Test
    void postWithDuplicatePlateReturns409() throws Exception {
        var carrierUser = saveUser("carrier-dup@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Dup Carrier");
        saveVehicle(carrier.getId(), "FFF-666", true);
        var token = loginAndGetToken("carrier-dup@example.com", "secret123");

        mockMvc.perform(post(VEHICLES_URL)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRegisterPayload("fff-666")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflict"));
    }

    @Test
    void getListReturnsOnlyActiveVehiclesByDefault() throws Exception {
        var carrierUser = saveUser("carrier-list@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "List Carrier");
        saveVehicle(carrier.getId(), "GGG-111", true);
        saveVehicle(carrier.getId(), "GGG-222", false);
        var token = loginAndGetToken("carrier-list@example.com", "secret123");

        mockMvc.perform(get(VEHICLES_URL)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].plate").value("GGG-111"));
    }

    @Test
    void getListWithIncludeInactiveReturnsAllOwnVehicles() throws Exception {
        var carrierUser = saveUser("carrier-list-all@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "List All Carrier");
        saveVehicle(carrier.getId(), "HHH-111", true);
        saveVehicle(carrier.getId(), "HHH-222", false);
        var token = loginAndGetToken("carrier-list-all@example.com", "secret123");

        mockMvc.perform(get(VEHICLES_URL)
                        .header("Authorization", "Bearer " + token)
                        .param("includeInactive", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getOwnVehicleReturns200() throws Exception {
        var carrierUser = saveUser("carrier-get@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Get Carrier");
        var vehicle = saveVehicle(carrier.getId(), "III-111", true);
        var token = loginAndGetToken("carrier-get@example.com", "secret123");

        mockMvc.perform(get(VEHICLES_URL + "/{id}", vehicle.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicle.getId()))
                .andExpect(jsonPath("$.plate").value("III-111"));
    }

    @Test
    void getPutAndPatchOnForeignVehicleReturn404() throws Exception {
        var carrierAUser = saveUser("carrier-a-fleet@example.com", "secret123", "carrier");
        var carrierA = saveCarrier(carrierAUser.getId(), "Carrier A");
        var tokenA = loginAndGetToken("carrier-a-fleet@example.com", "secret123");

        var carrierBUser = saveUser("carrier-b-fleet@example.com", "secret123", "carrier");
        var carrierB = saveCarrier(carrierBUser.getId(), "Carrier B");
        var foreignVehicle = saveVehicle(carrierB.getId(), "JJJ-999", true);

        mockMvc.perform(get(VEHICLES_URL + "/{id}", foreignVehicle.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());

        mockMvc.perform(put(VEHICLES_URL + "/{id}", foreignVehicle.getId())
                        .header("Authorization", "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdatePayload("JJJ-888")))
                .andExpect(status().isNotFound());

        mockMvc.perform(patch(VEHICLES_URL + "/{id}/deactivate", foreignVehicle.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNotFound());

        assertThat(vehicleRepository.findById(foreignVehicle.getId())).isPresent();
        assertThat(vehicleRepository.findById(foreignVehicle.getId()).orElseThrow().getCarrierId())
                .isEqualTo(carrierB.getId());
    }

    @Test
    void putOwnVehicleReplacesMutableFields() throws Exception {
        var carrierUser = saveUser("carrier-put@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Put Carrier");
        var vehicle = saveVehicle(carrier.getId(), "KKK-111", true);
        var token = loginAndGetToken("carrier-put@example.com", "secret123");

        mockMvc.perform(put(VEHICLES_URL + "/{id}", vehicle.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "plate": "kkk-999",
                                  "vehicleType": "VAN",
                                  "maxWeightKg": 900.00,
                                  "maxVolumeM3": 8.500,
                                  "refrigerated": false,
                                  "availabilityStatus": "UNAVAILABLE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plate").value("KKK-999"))
                .andExpect(jsonPath("$.vehicleType").value("VAN"))
                .andExpect(jsonPath("$.maxWeightKg").value(900.00))
                .andExpect(jsonPath("$.maxVolumeM3").value(8.500))
                .andExpect(jsonPath("$.refrigerated").value(false))
                .andExpect(jsonPath("$.availabilityStatus").value("UNAVAILABLE"));
    }

    @Test
    void putDoesNotChangeActiveFlag() throws Exception {
        var carrierUser = saveUser("carrier-put-active@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Put Active Carrier");
        var vehicle = saveVehicle(carrier.getId(), "LLL-111", false);
        var token = loginAndGetToken("carrier-put-active@example.com", "secret123");

        mockMvc.perform(put(VEHICLES_URL + "/{id}", vehicle.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdatePayload("LLL-222")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void patchDeactivateKeepsRecordAndSetsActiveFalse() throws Exception {
        var carrierUser = saveUser("carrier-deactivate@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Deactivate Carrier");
        var vehicle = saveVehicle(carrier.getId(), "MMM-111", true);
        var token = loginAndGetToken("carrier-deactivate@example.com", "secret123");

        mockMvc.perform(patch(VEHICLES_URL + "/{id}/deactivate", vehicle.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        assertThat(vehicleRepository.findById(vehicle.getId())).isPresent();
    }

    @Test
    void patchActivateSetsActiveTrue() throws Exception {
        var carrierUser = saveUser("carrier-activate@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Activate Carrier");
        var vehicle = saveVehicle(carrier.getId(), "NNN-111", false);
        var token = loginAndGetToken("carrier-activate@example.com", "secret123");

        mockMvc.perform(patch(VEHICLES_URL + "/{id}/activate", vehicle.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void legacyV1CarrierByIdRemainsAccessibleWithoutJwt() throws Exception {
        var carrierUser = saveUser("legacy-v1-carrier@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Legacy V1 Carrier");

        mockMvc.perform(get("/api/v1/carriers/{id}", carrier.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carrier.getId()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    private String validRegisterPayload(String plate) {
        return """
                {
                  "plate": "%s",
                  "vehicleType": "TRUCK",
                  "maxWeightKg": 1500.00,
                  "maxVolumeM3": 12.000,
                  "refrigerated": true
                }
                """.formatted(plate);
    }

    private String validUpdatePayload(String plate) {
        return """
                {
                  "plate": "%s",
                  "vehicleType": "TRUCK",
                  "maxWeightKg": 1200.00,
                  "refrigerated": true,
                  "availabilityStatus": "AVAILABLE"
                }
                """.formatted(plate);
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

        return objectMapper.readTree(response).get("accessToken").asText();
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
