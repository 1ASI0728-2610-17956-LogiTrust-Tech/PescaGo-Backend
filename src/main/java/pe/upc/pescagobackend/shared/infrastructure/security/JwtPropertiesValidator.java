package pe.upc.pescagobackend.shared.infrastructure.security;

import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class JwtPropertiesValidator {

    private final JwtProperties jwtProperties;
    private final Environment environment;

    public JwtPropertiesValidator(JwtProperties jwtProperties, Environment environment) {
        this.jwtProperties = jwtProperties;
        this.environment = environment;
    }

    @PostConstruct
    void validate() {
        String secret = jwtProperties.secret();
        String issuer = jwtProperties.issuer();
        String accessTokenTtl = jwtProperties.accessTokenTtl();

        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "JWT_SECRET is required. Set the JWT_SECRET environment variable or activate the local profile for development."
            );
        }

        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET must be at least 32 bytes for HS256.");
        }

        if (issuer == null || issuer.isBlank()) {
            throw new IllegalStateException("JWT_ISSUER must not be blank.");
        }

        if (!isLocalOrDevProfile() && isKnownInsecureDevelopmentSecret(secret)) {
            throw new IllegalStateException(
                    "JWT_SECRET must not use a development-only value outside local/dev profiles."
            );
        }

        JwtTtlParser.parse(accessTokenTtl);
    }

    private boolean isLocalOrDevProfile() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(profile -> profile.equalsIgnoreCase("local") || profile.equalsIgnoreCase("dev"));
    }

    private boolean isKnownInsecureDevelopmentSecret(String secret) {
        return secret.equals("local-dev-only-not-for-production-32chars-min");
    }
}
