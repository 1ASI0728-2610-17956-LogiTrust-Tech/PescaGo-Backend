package pe.upc.pescagobackend.fleet.domain.model.commands;

import pe.upc.pescagobackend.fleet.domain.model.FleetPlateRules;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleAvailabilityStatus;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleType;

import java.math.BigDecimal;

public record RegisterVehicleCommand(
        Long carrierId,
        String plate,
        VehicleType vehicleType,
        BigDecimal maxWeightKg,
        BigDecimal maxVolumeM3,
        boolean refrigerated,
        VehicleAvailabilityStatus availabilityStatus
) {
    public RegisterVehicleCommand {
        if (carrierId == null || carrierId <= 0) {
            throw new IllegalArgumentException("Carrier ID must be a positive number.");
        }
        plate = FleetPlateRules.normalize(plate);
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type is required.");
        }
        if (maxWeightKg == null || maxWeightKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Max weight must be greater than zero.");
        }
        if (maxVolumeM3 != null && maxVolumeM3.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Max volume must be greater than zero when provided.");
        }
        if (availabilityStatus == null) {
            availabilityStatus = VehicleAvailabilityStatus.AVAILABLE;
        }
    }
}
