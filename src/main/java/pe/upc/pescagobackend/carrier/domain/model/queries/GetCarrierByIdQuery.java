package pe.upc.pescagobackend.carrier.domain.model.queries;

public record GetCarrierByIdQuery(
    Long userId
) {
    public GetCarrierByIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Carrier ID must be a positive number.");
        }
    }
}
