package pe.upc.pescagobackend.request.interfaces.rest.transform;

import pe.upc.pescagobackend.request.domain.model.commands.CreateRequestCommand;
import pe.upc.pescagobackend.request.interfaces.rest.resources.CreateRequestResource;

public class CreateRequestCommandFromResourceAssembler {
    public static CreateRequestCommand toCommandFromResource(CreateRequestResource resource) {
        return new CreateRequestCommand(
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
