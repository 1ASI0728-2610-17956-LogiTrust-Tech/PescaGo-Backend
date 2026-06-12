package pe.upc.pescagobackend.hiredService.domain.model.queries;

public record GetHiredServiceByIdQuery(Long id) {
    public GetHiredServiceByIdQuery {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("The id cannot be null or negative");
        }
    }
}
