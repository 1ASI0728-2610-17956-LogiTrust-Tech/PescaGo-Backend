package pe.upc.pescagobackend.iam.interfaces.rest.resources;

public record LoginProfileResource(
        Long userId,
        String email,
        String role
) {
    public LoginProfileResource {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null or blank.");
        }
    }
}
