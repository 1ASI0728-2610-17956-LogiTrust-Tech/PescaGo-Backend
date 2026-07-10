package pe.upc.pescagobackend.carrier;

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
import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarrierByUserV2IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarrierRepository carrierRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCarrierByUserWithoutJwtReturns401() throws Exception {
        mockMvc.perform(get("/api/v2/carriers/by-user/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void carrierCanAccessOwnCarrierProfile() throws Exception {
        var carrierUser = saveUser("own-carrier@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Own Carrier");
        var token = loginAndGetToken("own-carrier@example.com", "secret123");

        mockMvc.perform(get("/api/v2/carriers/by-user/{userId}", carrierUser.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carrier.getId()))
                .andExpect(jsonPath("$.userId").value(carrierUser.getId()))
                .andExpect(jsonPath("$.name").value("Own Carrier"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void carrierCannotAccessAnotherUsersCarrierProfile() throws Exception {
        var carrierUser = saveUser("carrier-a@example.com", "secret123", "carrier");
        saveCarrier(carrierUser.getId(), "Carrier A");

        var otherUser = saveUser("carrier-b@example.com", "secret123", "carrier");
        saveCarrier(otherUser.getId(), "Carrier B");

        var token = loginAndGetToken("carrier-a@example.com", "secret123");

        mockMvc.perform(get("/api/v2/carriers/by-user/{userId}", otherUser.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.title").value("Forbidden"))
                .andExpect(jsonPath("$.detail").value("Access denied"));
    }

    @Test
    void adminCanAccessAnyUsersCarrierProfile() throws Exception {
        var adminUser = saveUser("admin@example.com", "secret123", "ADMIN");
        var carrierUser = saveUser("target-carrier@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Target Carrier");

        var token = loginAndGetToken("admin@example.com", "secret123");

        mockMvc.perform(get("/api/v2/carriers/by-user/{userId}", carrierUser.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carrier.getId()))
                .andExpect(jsonPath("$.userId").value(carrierUser.getId()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void authenticatedUserWithoutCarrierProfileReturns404() throws Exception {
        var entrepreneur = saveUser("no-carrier@example.com", "secret123", "entrepreneur");
        var token = loginAndGetToken("no-carrier@example.com", "secret123");

        mockMvc.perform(get("/api/v2/carriers/by-user/{userId}", entrepreneur.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void legacyV1CarrierByIdRemainsAccessibleWithoutJwt() throws Exception {
        var carrierUser = saveUser("legacy-carrier@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Legacy Carrier");

        mockMvc.perform(get("/api/v1/carriers/{id}", carrier.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carrier.getId()))
                .andExpect(jsonPath("$.userId").value(carrierUser.getId()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void legacyV1CarrierByUserIdReturnsCarrierWithoutJwt() throws Exception {
        var carrierUser = saveUser("legacy-by-user@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Legacy By User Carrier");

        mockMvc.perform(get("/api/v1/carriers/by-user/{userId}", carrierUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carrier.getId()))
                .andExpect(jsonPath("$.userId").value(carrierUser.getId()))
                .andExpect(jsonPath("$.name").value("Legacy By User Carrier"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void legacyV1CarrierByUserIdReturns404WhenUserHasNoCarrierProfile() throws Exception {
        var entrepreneur = saveUser("legacy-no-carrier@example.com", "secret123", "entrepreneur");

        mockMvc.perform(get("/api/v1/carriers/by-user/{userId}", entrepreneur.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void legacyV1CarrierByUserIdDoesNotTreatUserIdAsCarrierId() throws Exception {
        var carrierUser = saveUser("legacy-mismatch@example.com", "secret123", "carrier");
        var carrier = saveCarrier(carrierUser.getId(), "Mismatch Carrier");

        mockMvc.perform(get("/api/v1/carriers/{id}", carrierUser.getId()))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/v1/carriers/by-user/{userId}", carrierUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carrier.getId()));
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
}
