package pe.upc.pescagobackend.hiredService.interfaces.rest.resources;

import pe.upc.pescagobackend.hiredService.domain.model.aggregates.CarrierData;

import java.time.LocalDateTime;

public record HiredServiceResource(
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
}
