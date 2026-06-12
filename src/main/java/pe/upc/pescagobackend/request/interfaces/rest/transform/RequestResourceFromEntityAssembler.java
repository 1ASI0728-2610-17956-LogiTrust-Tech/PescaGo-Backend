package pe.upc.pescagobackend.request.interfaces.rest.transform;

import pe.upc.pescagobackend.request.interfaces.rest.resources.RequestResource;

public class RequestResourceFromEntityAssembler {
    public static RequestResource toResourceFromEntity(pe.upc.pescagobackend.request.domain.model.aggregates.Request entity) {
        return new RequestResource(
            entity.getId(),
            entity.getEntrepreneurId(),
            entity.getEntrepreneurName(),
            entity.getCarrierId(),
            entity.getCarrierName(),
            entity.getPackageDescription(),
            entity.getQuantity(),
            entity.getWeightTotal(),
            entity.getPickupLocation(),
            entity.getDeliveryLocation(),
            entity.getPickupDateTime(),
            entity.getPrice(),
            entity.getStatus(),
            entity.getDimensions()
        );
    }
}
