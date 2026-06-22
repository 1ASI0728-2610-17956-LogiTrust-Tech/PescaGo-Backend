package pe.upc.pescagobackend.carrier.domain.model.queries;

public record GetCarrierByUserIdQuery(Long userId) {
    public GetCarrierByUserIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number.");
        }
    }
}
