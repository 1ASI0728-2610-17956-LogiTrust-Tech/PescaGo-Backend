package pe.upc.pescagobackend.carrier.interfaces.rest.resources;

public record CarrierResource(
    Long id,
    Long userId,
    String name,
    String description
) {
    public CarrierResource {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Carrier ID must be a positive number.");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank.");
        }

    }
}
