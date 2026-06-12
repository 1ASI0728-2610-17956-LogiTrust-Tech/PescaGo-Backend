package pe.upc.pescagobackend.entrepreneur.domain.model.queries;

public record GetEntreprenuerByIdQuery(
    Long userId
) {
    public GetEntreprenuerByIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Entreprenuer ID must be a positive number.");
        }
    }
}
