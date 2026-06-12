package pe.upc.pescagobackend.request.domain.model.queries;

public record GetRequestByIdQuery(Long id) {
    public GetRequestByIdQuery {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("The id cannot be null or negative");
        }
    }
}
