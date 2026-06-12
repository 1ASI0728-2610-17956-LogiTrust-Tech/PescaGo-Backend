package pe.upc.pescagobackend.hiredService.domain.model.queries;

public record GetHiredServicesByCarrierIdQuery(
    Long carrierId
) {
    public GetHiredServicesByCarrierIdQuery {
        if (carrierId == null || carrierId <= 0) {
            throw new IllegalArgumentException("Carrier ID must be a positive number.");
        }
    }
}