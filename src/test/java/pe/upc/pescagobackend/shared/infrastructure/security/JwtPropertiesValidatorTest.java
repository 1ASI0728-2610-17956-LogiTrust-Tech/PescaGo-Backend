package pe.upc.pescagobackend.shared.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class JwtPropertiesValidatorTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(JwtPropertiesValidatorTest.TestConfiguration.class)
            .withBean(JwtPropertiesValidator.class);

    @Test
    void rejectsJwtSecretShorterThan32Bytes() {
        contextRunner
                .withPropertyValues(
                        "jwt.secret=short",
                        "jwt.issuer=pescago-test",
                        "jwt.access-token-ttl=1h"
                )
                .run(context -> assertThat(context).hasFailed());
    }

    @Test
    void rejectsMalformedJwtAccessTokenTtl() {
        contextRunner
                .withPropertyValues(
                        "jwt.secret=test-jwt-secret-key-minimum-32-characters-long",
                        "jwt.issuer=pescago-test",
                        "jwt.access-token-ttl=not-a-valid-ttl"
                )
                .run(context -> assertThat(context).hasFailed());
    }

    @Configuration
    @EnableConfigurationProperties(JwtProperties.class)
    static class TestConfiguration {
    }
}
