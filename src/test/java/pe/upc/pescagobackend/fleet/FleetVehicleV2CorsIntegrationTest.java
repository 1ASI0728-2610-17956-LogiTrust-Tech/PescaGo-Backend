package pe.upc.pescagobackend.fleet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FleetVehicleV2CorsIntegrationTest {

    private static final String FRONTEND_ORIGIN = "http://localhost:4200";
    private static final String ACTIVATE_URL = "/api/v2/fleet/vehicles/1/activate";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void fleetActivatePatchPreflightAllowsFrontendOrigin() throws Exception {
        mockMvc.perform(options(ACTIVATE_URL)
                        .header("Origin", FRONTEND_ORIGIN)
                        .header("Access-Control-Request-Method", "PATCH")
                        .header("Access-Control-Request-Headers", "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", FRONTEND_ORIGIN))
                .andExpect(header().string("Access-Control-Allow-Methods", containsString("PATCH")))
                .andExpect(header().string("Access-Control-Allow-Headers", containsString("authorization")))
                .andExpect(header().string("Access-Control-Allow-Headers", containsString("content-type")));
    }
}
