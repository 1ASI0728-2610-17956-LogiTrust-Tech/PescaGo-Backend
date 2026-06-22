package pe.upc.pescagobackend.fleet.domain.model;

import java.util.Locale;

public final class FleetPlateRules {

    private static final int MAX_PLATE_LENGTH = 20;

    private FleetPlateRules() {
    }

    public static String normalize(String plate) {
        if (plate == null) {
            throw new IllegalArgumentException("Plate cannot be null.");
        }
        String normalized = plate.trim().toUpperCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Plate cannot be blank.");
        }
        if (normalized.length() > MAX_PLATE_LENGTH) {
            throw new IllegalArgumentException("Plate must not exceed 20 characters.");
        }
        return normalized;
    }
}
