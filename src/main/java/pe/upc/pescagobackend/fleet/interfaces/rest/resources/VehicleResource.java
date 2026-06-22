package pe.upc.pescagobackend.fleet.interfaces.rest.resources;

import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleAvailabilityStatus;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleType;

import java.math.BigDecimal;

public record VehicleResource(
        Long id,
        String plate,
        VehicleType vehicleType,
        BigDecimal maxWeightKg,
        BigDecimal maxVolumeM3,
        boolean refrigerated,
        VehicleAvailabilityStatus availabilityStatus,
        boolean active
) {
    public VehicleResource {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Vehicle ID must be a positive number.");
        }
        if (plate == null || plate.isBlank()) {
            throw new IllegalArgumentException("Plate cannot be null or blank.");
        }
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type is required.");
        }
        if (maxWeightKg == null) {
            throw new IllegalArgumentException("Max weight is required.");
        }
        if (availabilityStatus == null) {
            throw new IllegalArgumentException("Availability status is required.");
        }
    }
}
