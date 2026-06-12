package pe.upc.pescagobackend.hiredService.domain.model.queries;

public record GetHiredServicesByEntrepreneurIdQuery(
    Long entrepreneurId
) {
    public GetHiredServicesByEntrepreneurIdQuery {
        if (entrepreneurId == null || entrepreneurId <= 0) {
            throw new IllegalArgumentException("Entrepreneur ID must be a positive number.");
        }
    }
}