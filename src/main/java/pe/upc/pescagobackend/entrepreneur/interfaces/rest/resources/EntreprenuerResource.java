package pe.upc.pescagobackend.entrepreneur.interfaces.rest.resources;

public record EntreprenuerResource(
    Long id,
    Long userId,
    String name
) {
    public EntreprenuerResource {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Entreprenuer ID must be a positive number.");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }

    }
}
