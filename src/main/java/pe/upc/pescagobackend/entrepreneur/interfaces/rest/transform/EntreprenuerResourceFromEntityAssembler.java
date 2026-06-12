package pe.upc.pescagobackend.entrepreneur.interfaces.rest.transform;

import pe.upc.pescagobackend.entrepreneur.domain.model.aggregates.Entreprenuer;
import pe.upc.pescagobackend.entrepreneur.interfaces.rest.resources.EntreprenuerResource;

public class EntreprenuerResourceFromEntityAssembler {
    public static EntreprenuerResource toResourceFromEntity(Entreprenuer entity) {
        return new EntreprenuerResource(
            entity.getId(),
            entity.getUserId(),
            entity.getName()
        );
    }
}
