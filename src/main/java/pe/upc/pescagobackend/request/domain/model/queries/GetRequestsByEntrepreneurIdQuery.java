package pe.upc.pescagobackend.request.domain.model.queries;

public record GetRequestsByEntrepreneurIdQuery(
    Long entrepreneurId
) {
    public GetRequestsByEntrepreneurIdQuery {
        if (entrepreneurId == null || entrepreneurId <= 0) {
            throw new IllegalArgumentException("Entrepreneur ID must be a positive number.");
        }
    }
}