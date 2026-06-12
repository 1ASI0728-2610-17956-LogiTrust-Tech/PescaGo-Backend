package pe.upc.pescagobackend.iam.domain.model.commands;

public record CreateUserCommand(
    String username,
    String password,
    String email,
    String role
) {
    public CreateUserCommand {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null or blank.");
        }
    }
}
