package pe.upc.pescagobackend.shared.infrastructure.blockchain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class BlockchainPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfiguration.class);

    @Test
    void loadsConfiguredTimeouts() {
        contextRunner
                .withPropertyValues(
                        "blockchain.enabled=false",
                        "blockchain.base-url=http://localhost:3001",
                        "blockchain.mine-after-record=false",
                        "blockchain.connect-timeout-ms=2000",
                        "blockchain.read-timeout-ms=3000"
                )
                .run(context -> {
                    var properties = context.getBean(BlockchainProperties.class);
                    assertThat(properties.connectTimeoutMs()).isEqualTo(2000);
                    assertThat(properties.readTimeoutMs()).isEqualTo(3000);
                });
    }

    @Test
    void allowsOverridingTimeouts() {
        contextRunner
                .withPropertyValues(
                        "blockchain.enabled=true",
                        "blockchain.base-url=http://localhost:3002",
                        "blockchain.mine-after-record=false",
                        "blockchain.connect-timeout-ms=5000",
                        "blockchain.read-timeout-ms=8000"
                )
                .run(context -> {
                    var properties = context.getBean(BlockchainProperties.class);
                    assertThat(properties.connectTimeoutMs()).isEqualTo(5000);
                    assertThat(properties.readTimeoutMs()).isEqualTo(8000);
                });
    }

    @Configuration
    @EnableConfigurationProperties(BlockchainProperties.class)
    static class TestConfiguration {
    }
}
