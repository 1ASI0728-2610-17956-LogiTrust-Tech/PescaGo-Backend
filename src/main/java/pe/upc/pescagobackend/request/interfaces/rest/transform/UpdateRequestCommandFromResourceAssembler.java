package pe.upc.pescagobackend.request.interfaces.rest.transform;

import pe.upc.pescagobackend.request.domain.model.commands.UpdateRequestCommand;
import pe.upc.pescagobackend.request.interfaces.rest.resources.UpdateRequestResource;

public class UpdateRequestCommandFromResourceAssembler {
    public static UpdateRequestCommand toCommandFromResource (Long id, UpdateRequestResource resource) {
        return new UpdateRequestCommand(
                id,
                resource.entrepreneurId(),
                resource.entrepreneurName(),
                resource.carrierId(),
                resource.carrierName(),
                resource.packageDescription(),
                resource.quantity(),
                resource.weightTotal(),
                resource.pickupLocation(),
                resource.deliveryLocation(),
                resource.pickupDateTime(),
                resource.price(),
                resource.status(),
                resource.dimensions()
        );
    }
}
