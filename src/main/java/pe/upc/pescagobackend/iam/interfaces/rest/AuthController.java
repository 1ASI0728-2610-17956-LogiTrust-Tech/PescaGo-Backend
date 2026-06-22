package pe.upc.pescagobackend.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.upc.pescagobackend.iam.application.internal.authenticationservices.PasswordAuthenticationService;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.LoginProfileResource;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.LoginRequest;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.LoginResponse;
import pe.upc.pescagobackend.shared.application.RoleCompatibilityMapper;
import pe.upc.pescagobackend.shared.infrastructure.security.JwtService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v2/auth", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Auth v2", description = "Authentication endpoints for PescaGo v2")
public class AuthController {

    private final PasswordAuthenticationService passwordAuthenticationService;
    private final JwtService jwtService;

    public AuthController(
            PasswordAuthenticationService passwordAuthenticationService,
            JwtService jwtService
    ) {
        this.passwordAuthenticationService = passwordAuthenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login v2", description = "Authenticate with email and password and receive a JWT access token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var authenticatedUser = passwordAuthenticationService.authenticate(request.email(), request.password());
        if (authenticatedUser.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        var user = authenticatedUser.get();
        var canonicalRole = RoleCompatibilityMapper.toCanonicalRole(user.getRole());
        var accessToken = jwtService.generateAccessToken(user);
        var profile = new LoginProfileResource(user.getId(), user.getEmail(), canonicalRole.name());

        return ResponseEntity.ok(new LoginResponse(accessToken, "Bearer", profile));
    }
}
