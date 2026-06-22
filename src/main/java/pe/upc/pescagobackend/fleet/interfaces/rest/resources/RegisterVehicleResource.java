package pe.upc.pescagobackend.fleet.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleAvailabilityStatus;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleType;

import java.math.BigDecimal;

public record RegisterVehicleResource(
        @NotBlank @Size(max = 20) String plate,
        @NotNull VehicleType vehicleType,
        @NotNull @Positive BigDecimal maxWeightKg,
        @Positive BigDecimal maxVolumeM3,
        @NotNull Boolean refrigerated,
        VehicleAvailabilityStatus availabilityStatus
) {
    public RegisterVehicleResource {
        if (plate == null || plate.isBlank()) {
            throw new IllegalArgumentException("Plate is required.");
        }
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type is required.");
        }
        if (maxWeightKg == null) {
            throw new IllegalArgumentException("Max weight is required.");
        }
        if (maxWeightKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Max weight must be greater than zero.");
        }
        if (maxVolumeM3 != null && maxVolumeM3.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Max volume must be greater than zero when provided.");
        }
        if (refrigerated == null) {
            throw new IllegalArgumentException("Refrigerated flag is required.");
        }
    }
}
