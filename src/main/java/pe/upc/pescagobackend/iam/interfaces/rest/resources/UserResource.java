package pe.upc.pescagobackend.iam.interfaces.rest.resources;

public record UserResource(
    Long id,
    String username,
    String email,
    String password,
    String role
) {
    public UserResource {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank.");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null or blank.");
        }
    }
}
