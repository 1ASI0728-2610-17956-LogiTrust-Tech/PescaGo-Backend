package pe.upc.pescagobackend.iam.interfaces.rest.transform;

import pe.upc.pescagobackend.iam.domain.model.commands.CreateUserCommand;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.CreateUserResource;

public class CreateUserCommandFromResourceAssembler {
    public static CreateUserCommand toCommandFromResource(CreateUserResource resource) {
        return new CreateUserCommand(
                resource.username(),
                resource.password(),
                resource.email(),
                resource.role()
        );
    }
}
