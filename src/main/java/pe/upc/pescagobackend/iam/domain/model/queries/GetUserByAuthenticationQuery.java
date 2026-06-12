package pe.upc.pescagobackend.iam.domain.model.queries;

public record GetUserByAuthenticationQuery(
        String email,
        String password
) {
    public GetUserByAuthenticationQuery {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank.");
        }
    }
}
