package pe.upc.pescagobackend.iam.interfaces.rest.resources;

public record LoginResponse(
        String accessToken,
        String tokenType,
        LoginProfileResource profile
) {
    public LoginResponse {
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("Access token cannot be null or blank.");
        }
        if (tokenType == null || tokenType.isBlank()) {
            throw new IllegalArgumentException("Token type cannot be null or blank.");
        }
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null.");
        }
    }
}
