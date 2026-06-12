package pe.upc.pescagobackend.hiredService.domain.model.commands;


import pe.upc.pescagobackend.hiredService.domain.model.aggregates.CarrierData;

import java.time.LocalDateTime;

public record UpdateHiredServiceCommand(
        Long id,
        Long requestId,
        Long entrepreneurId,
        String entrepreneurName,
        Long carrierId,
        String carrierName,
        String packageDescription,
        LocalDateTime pickupDateTime,
        String paymentMethod,
        String status,
        CarrierData carrierData
) {
    public UpdateHiredServiceCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("The ID must be a positive number");
        }
        if (requestId == null || requestId <= 0) {
            throw new IllegalArgumentException("The request ID must be a positive number");
        }
        if (entrepreneurId == null || entrepreneurId <= 0) {
            throw new IllegalArgumentException("The entrepreneur ID must be a positive number");
        }
        if (entrepreneurName == null || entrepreneurName.isBlank()) {
            throw new IllegalArgumentException("The entrepreneur name cannot be empty");
        }
        if (carrierId == null || carrierId <= 0) {
            throw new IllegalArgumentException("The carrier ID must be a positive number");
        }
        if (carrierName == null || carrierName.isBlank()) {
            throw new IllegalArgumentException("The carrier name cannot be empty");
        }
        if (packageDescription == null || packageDescription.isBlank()) {
            throw new IllegalArgumentException("The package description cannot be empty");
        }
        if (pickupDateTime == null) {
            throw new IllegalArgumentException("The pickup date and time cannot be empty");
        }
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("The payment method cannot be empty");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("The status cannot be empty");
        }
        if (carrierData == null) {
            throw new IllegalArgumentException("Carrier data cannot be null");
        }
    }
}
