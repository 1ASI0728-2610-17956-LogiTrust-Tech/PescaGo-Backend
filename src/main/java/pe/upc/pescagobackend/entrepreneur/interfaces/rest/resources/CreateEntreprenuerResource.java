package pe.upc.pescagobackend.entrepreneur.interfaces.rest.resources;

public record CreateEntreprenuerResource(
    Long userId,
    String name
) {
    public CreateEntreprenuerResource {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }

    }
}
