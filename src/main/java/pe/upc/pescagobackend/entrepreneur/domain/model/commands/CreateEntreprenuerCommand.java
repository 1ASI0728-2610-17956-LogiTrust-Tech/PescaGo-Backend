package pe.upc.pescagobackend.entrepreneur.domain.model.commands;

public record CreateEntreprenuerCommand(
        Long userId,
        String name
) {
    public CreateEntreprenuerCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
    }

}
