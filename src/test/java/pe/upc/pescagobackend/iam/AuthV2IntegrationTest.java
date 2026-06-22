package pe.upc.pescagobackend.iam;

import com.fasterxml.jackson.databind.JsonNode;
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
import pe.upc.pescagobackend.iam.application.internal.authenticationservices.PasswordAuthenticationService;
import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthV2IntegrationTest {

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
    void loginV2SucceedsWithBcryptPassword() throws Exception {
        var user = saveUser("carrier@example.com", passwordEncoder.encode("secret123"), "carrier");
        saveCarrier(user.getId(), "Carrier One");

        var response = mockMvc.perform(post("/api/v2/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"carrier@example.com","password":"secret123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.profile.userId").value(user.getId()))
                .andExpect(jsonPath("$.profile.email").value("carrier@example.com"))
                .andExpect(jsonPath("$.profile.role").value("CARRIER"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode body = objectMapper.readTree(response);
        String token = body.get("accessToken").asText();

        mockMvc.perform(get("/api/v2/users/me/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.email").value("carrier@example.com"))
                .andExpect(jsonPath("$.role").value("CARRIER"))
                .andExpect(jsonPath("$.profile.type").value("carrier"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void loginV2WithInvalidCredentialsReturns401() throws Exception {
        saveUser("invalid@example.com", passwordEncoder.encode("secret123"), "carrier");

        mockMvc.perform(post("/api/v2/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"invalid@example.com","password":"wrong-password"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void legacyV1AuthenticationStillWorks() throws Exception {
        saveUser("legacy@example.com", "plain-password", "entrepreneur");

        mockMvc.perform(get("/api/v1/users/authentication")
                        .param("email", "legacy@example.com")
                        .param("password", "plain-password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("legacy@example.com"))
                .andExpect(jsonPath("$.role").value("entrepreneur"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void legacyPasswordIsRehashedToBcryptAfterSuccessfulLogin() throws Exception {
        var user = saveUser("rehash@example.com", "legacy-plain", "carrier");

        mockMvc.perform(get("/api/v1/users/authentication")
                        .param("email", "rehash@example.com")
                        .param("password", "legacy-plain"))
                .andExpect(status().isOk());

        User reloaded = userRepository.findById(user.getId()).orElseThrow();
        assertThat(PasswordAuthenticationService.isBcryptHash(reloaded.getPassword())).isTrue();
        assertThat(passwordEncoder.matches("legacy-plain", reloaded.getPassword())).isTrue();
    }

    @Test
    void profileWithoutTokenReturns401() throws Exception {
        mockMvc.perform(get("/api/v2/users/me/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void v2PreflightAllowsAuthorizationHeaderFromFrontendOrigin() throws Exception {
        mockMvc.perform(options("/api/v2/users/me/profile")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "Authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"))
                .andExpect(header().string("Access-Control-Allow-Methods", containsString("GET")))
                .andExpect(header().string("Access-Control-Allow-Headers", containsString("Authorization")));
    }

    @Test
    void v1EndpointsRemainAccessibleWithoutJwt() throws Exception {
        mockMvc.perform(get("/api/v1/carriers"))
                .andExpect(status().isOk());
    }

    private User saveUser(String email, String password, String role) {
        User user = new User();
        user.setUsername(email);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return userRepository.save(user);
    }

    private void saveCarrier(Long userId, String name) {
        Carrier carrier = new Carrier();
        carrier.setUserId(userId);
        carrier.setName(name);
        carrier.setDescription("Test carrier");
        carrierRepository.save(carrier);
    }
}
