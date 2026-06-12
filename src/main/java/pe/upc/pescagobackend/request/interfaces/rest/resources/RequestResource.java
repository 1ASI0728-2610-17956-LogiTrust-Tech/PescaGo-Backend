package pe.upc.pescagobackend.request.interfaces.rest.resources;

import pe.upc.pescagobackend.request.domain.model.aggregates.Dimensions;

import java.time.LocalDateTime;

public record RequestResource(
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
) {
}
