package pe.upc.pescagobackend.carrier.interfaces.rest.transform;

import pe.upc.pescagobackend.carrier.domain.model.commands.CreateCarrierCommand;
import pe.upc.pescagobackend.carrier.interfaces.rest.resources.CreateCarrierResource;

public class CreateCarrierCommandFromResourceAssembler {
    public static CreateCarrierCommand toCommandFromResource(CreateCarrierResource resource) {
        return new CreateCarrierCommand(
                resource.userId(),
                resource.name(),
                resource.description()
        );
    }
}
