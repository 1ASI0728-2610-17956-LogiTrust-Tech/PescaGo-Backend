package pe.upc.pescagobackend.iam.interfaces.rest.resources;

public record BusinessProfileResource(
        String type,
        Long id,
        Long userId,
        String name,
        String description
) {
    public BusinessProfileResource {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Profile type cannot be null or blank.");
        }
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Profile ID must be a positive number.");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }
    }
}
