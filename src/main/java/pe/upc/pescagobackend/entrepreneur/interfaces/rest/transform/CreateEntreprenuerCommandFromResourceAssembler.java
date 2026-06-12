package pe.upc.pescagobackend.entrepreneur.interfaces.rest.transform;

import pe.upc.pescagobackend.entrepreneur.domain.model.commands.CreateEntreprenuerCommand;
import pe.upc.pescagobackend.entrepreneur.interfaces.rest.resources.CreateEntreprenuerResource;

public class CreateEntreprenuerCommandFromResourceAssembler {
    public static CreateEntreprenuerCommand toCommandFromResource(CreateEntreprenuerResource resource) {
        return new CreateEntreprenuerCommand(
                resource.userId(),
                resource.name()
        );
    }
}
