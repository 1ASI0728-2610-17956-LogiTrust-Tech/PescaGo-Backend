package pe.upc.pescagobackend.shared.application;

import pe.upc.pescagobackend.shared.domain.model.enums.Role;

public final class RoleCompatibilityMapper {

    private RoleCompatibilityMapper() {
    }

    public static Role toCanonicalRole(String legacyRole) {
        if (legacyRole == null || legacyRole.isBlank()) {
            return Role.LEGACY_USER;
        }
        return switch (legacyRole.trim().toLowerCase()) {
            case "carrier" -> Role.CARRIER;
            case "entrepreneur" -> Role.ENTREPRENEUR;
            case "admin" -> Role.ADMIN;
            default -> Role.LEGACY_USER;
        };
    }
}
