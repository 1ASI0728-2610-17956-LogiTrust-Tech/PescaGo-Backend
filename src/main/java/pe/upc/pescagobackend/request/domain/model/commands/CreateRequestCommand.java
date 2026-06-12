package pe.upc.pescagobackend.request.domain.model.commands;

import pe.upc.pescagobackend.request.domain.model.aggregates.Dimensions;

import java.time.LocalDateTime;

public record CreateRequestCommand(
    Long entrepreneurId,
    String entrepreneurName,
    Long carrierId,
    String carrierName,
    String packageDescription,
    Integer quantity,
    Double weightTotal,
    String pickupLocation,
    String deliveryLocation,
    LocalDateTime pickupDateTime,
    Double price,
    String status,

    Dimensions dimensions
) {
    public CreateRequestCommand {
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
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("The quantity must be a positive number");
        }
        if (weightTotal == null || weightTotal <= 0) {
            throw new IllegalArgumentException("The total weight must be a positive number");
        }
        if (pickupLocation == null || pickupLocation.isBlank()) {
            throw new IllegalArgumentException("The pickup location cannot be empty");
        }
        if (deliveryLocation == null || deliveryLocation.isBlank()) {
            throw new IllegalArgumentException("The delivery location cannot be empty");
        }
        if (pickupDateTime == null) {
            throw new IllegalArgumentException("The pickup date and time cannot be empty");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("The price must be a non-negative number");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("The status cannot be empty");
        }

        if (dimensions == null) {
            throw new IllegalArgumentException("Dimensions cannot be null");
        }
    }
}
