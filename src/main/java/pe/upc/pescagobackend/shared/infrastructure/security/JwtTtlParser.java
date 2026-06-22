package pe.upc.pescagobackend.shared.infrastructure.security;

import java.time.Duration;

public final class JwtTtlParser {

    private JwtTtlParser() {
    }

    public static Duration parse(String ttl) {
        if (ttl == null || ttl.isBlank()) {
            throw new IllegalArgumentException("JWT access token TTL must not be blank.");
        }
        String normalized = ttl.trim().toLowerCase();
        try {
            if (normalized.endsWith("ms")) {
                return Duration.ofMillis(Long.parseLong(normalized.substring(0, normalized.length() - 2)));
            }
            if (normalized.endsWith("s")) {
                return Duration.ofSeconds(Long.parseLong(normalized.substring(0, normalized.length() - 1)));
            }
            if (normalized.endsWith("m")) {
                return Duration.ofMinutes(Long.parseLong(normalized.substring(0, normalized.length() - 1)));
            }
            if (normalized.endsWith("h")) {
                return Duration.ofHours(Long.parseLong(normalized.substring(0, normalized.length() - 1)));
            }
            if (normalized.endsWith("d")) {
                return Duration.ofDays(Long.parseLong(normalized.substring(0, normalized.length() - 1)));
            }
            return Duration.parse("PT" + normalized.toUpperCase());
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Invalid JWT access token TTL format: " + ttl, ex);
        }
    }
}
