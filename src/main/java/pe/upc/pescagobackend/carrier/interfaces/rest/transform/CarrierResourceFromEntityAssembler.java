package pe.upc.pescagobackend.carrier.interfaces.rest.transform;

import pe.upc.pescagobackend.carrier.domain.model.aggregates.Carrier;
import pe.upc.pescagobackend.carrier.interfaces.rest.resources.CarrierResource;

public class CarrierResourceFromEntityAssembler {
    public static CarrierResource toResourceFromEntity(Carrier entity) {
        return new CarrierResource(
            entity.getId(),
            entity.getUserId(),
            entity.getName(),
            entity.getDescription()
        );
    }
}
