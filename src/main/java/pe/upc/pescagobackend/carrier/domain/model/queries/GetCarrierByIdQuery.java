package pe.upc.pescagobackend.carrier.domain.model.queries;

public record GetCarrierByIdQuery(
        Long id
) {
    public GetCarrierByIdQuery {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Carrier ID must be a positive number.");
        }
    }
}