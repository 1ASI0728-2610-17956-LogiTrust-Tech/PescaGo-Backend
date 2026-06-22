package pe.upc.pescagobackend.fleet.domain.model.queries;

public record GetOwnVehiclesQuery(Long carrierId, boolean includeInactive) {
    public GetOwnVehiclesQuery {
        if (carrierId == null || carrierId <= 0) {
            throw new IllegalArgumentException("Carrier ID must be a positive number.");
        }
    }
}
