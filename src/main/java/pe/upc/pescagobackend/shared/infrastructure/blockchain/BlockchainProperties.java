package pe.upc.pescagobackend.shared.infrastructure.blockchain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "blockchain")
public record BlockchainProperties(
        boolean enabled,
        String baseUrl,
        boolean mineAfterRecord,
        int connectTimeoutMs,
        int readTimeoutMs
) {
}
