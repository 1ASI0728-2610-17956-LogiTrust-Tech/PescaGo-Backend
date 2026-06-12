package pe.upc.pescagobackend.request.domain.model.queries;

public record GetRequestsByCarrierIdQuery(
    Long carrierId
) {
    public GetRequestsByCarrierIdQuery {
        if (carrierId == null || carrierId <= 0) {
            throw new IllegalArgumentException("Carrier ID must be a positive number.");
        }
    }
}