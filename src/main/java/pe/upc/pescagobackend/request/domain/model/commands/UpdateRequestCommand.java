package pe.upc.pescagobackend.request.domain.model.commands;

import pe.upc.pescagobackend.request.domain.model.aggregates.Dimensions;

import java.time.LocalDateTime;

public record UpdateRequestCommand(
        Long id,
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
)
{
    public UpdateRequestCommand {
        if (entrepreneurId == null || entrepreneurId <= 0) {
            throw new IllegalArgumentException("Entrepreneur ID is required");
        }
        if (carrierId == null || carrierId <= 0) {
            throw new IllegalArgumentException("Carrier ID is required");
        }
        if (packageDescription == null || packageDescription.isBlank()) {
            throw new IllegalArgumentException("Package description cannot be empty");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (weightTotal == null || weightTotal <= 0) {
            throw new IllegalArgumentException("Weight total must be greater than zero");
        }
        if (pickupLocation == null || pickupLocation.isBlank()) {
            throw new IllegalArgumentException("Pickup location cannot be empty");
        }
        if (deliveryLocation == null || deliveryLocation.isBlank()) {
            throw new IllegalArgumentException("Delivery location cannot be empty");
        }
        if (pickupDateTime == null) {
            throw new IllegalArgumentException("Pickup date and time cannot be empty");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price must be a non-negative value");
        }
    }
}
