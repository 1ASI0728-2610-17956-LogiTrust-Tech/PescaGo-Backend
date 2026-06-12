package pe.upc.pescagobackend.hiredService.interfaces.rest.transform;

import pe.upc.pescagobackend.hiredService.domain.model.aggregates.HiredService;
import pe.upc.pescagobackend.hiredService.interfaces.rest.resources.HiredServiceResource;

public class HiredServiceResourceFromEntityAssembler {
    public static HiredServiceResource toResourceFromEntity(HiredService entity) {
        return new HiredServiceResource(
            entity.getId(),
            entity.getRequestId(),
            entity.getEntrepreneurId(),
            entity.getEntrepreneurName(),
            entity.getCarrierId(),
            entity.getCarrierName(),
            entity.getPackageDescription(),
            entity.getPickupDateTime(),
            entity.getPaymentMethod(),
            entity.getStatus(),
            entity.getCarrierData()
        );
    }
}
