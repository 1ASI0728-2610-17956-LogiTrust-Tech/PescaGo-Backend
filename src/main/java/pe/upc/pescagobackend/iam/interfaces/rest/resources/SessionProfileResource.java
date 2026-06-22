package pe.upc.pescagobackend.iam.interfaces.rest.resources;

public record SessionProfileResource(
        Long userId,
        String username,
        String email,
        String role,
        BusinessProfileResource profile
) {
    public SessionProfileResource {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number.");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null or blank.");
        }
    }
}
