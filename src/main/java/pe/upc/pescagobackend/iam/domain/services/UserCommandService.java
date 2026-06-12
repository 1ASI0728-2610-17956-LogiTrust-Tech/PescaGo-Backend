package pe.upc.pescagobackend.iam.domain.services;

import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.domain.model.commands.CreateUserCommand;
import pe.upc.pescagobackend.iam.domain.model.commands.DeleteUserCommand;

import java.util.Optional;

public interface UserCommandService {
    Optional<User> handle(CreateUserCommand command);
    void handle(DeleteUserCommand command);
}
