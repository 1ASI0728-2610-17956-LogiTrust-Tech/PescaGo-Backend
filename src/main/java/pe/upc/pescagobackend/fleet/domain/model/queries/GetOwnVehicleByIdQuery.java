package pe.upc.pescagobackend.fleet.domain.model.queries;

public record GetOwnVehicleByIdQuery(Long vehicleId, Long carrierId) {
    public GetOwnVehicleByIdQuery {
        if (vehicleId == null || vehicleId <= 0) {
            throw new IllegalArgumentException("Vehicle ID must be a positive number.");
        }
        if (carrierId == null || carrierId <= 0) {
            throw new IllegalArgumentException("Carrier ID must be a positive number.");
        }
    }
}
