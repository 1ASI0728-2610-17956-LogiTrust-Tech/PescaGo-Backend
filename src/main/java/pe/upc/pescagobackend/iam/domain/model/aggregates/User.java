package pe.upc.pescagobackend.iam.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pe.upc.pescagobackend.iam.domain.model.commands.CreateUserCommand;
import pe.upc.pescagobackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Getter
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private String email;
    @Setter
    private String role;

    public User() {
        this.username = "";
        this.password = "";
        this.email = "";
        this.role = "USER"; // Default role
    }

    public User(CreateUserCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("CreateUserCommand cannot be null.");
        }
        this.username = command.username();
        this.password = command.password();
        this.email = command.email();
        this.role = command.role();
    }
}
