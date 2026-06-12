package pe.upc.pescagobackend.iam.interfaces.rest.transform;

import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User entity) {
        return new UserResource(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getRole()
        );
    }
}
