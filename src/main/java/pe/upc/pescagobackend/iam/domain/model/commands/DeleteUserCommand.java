package pe.upc.pescagobackend.iam.domain.model.commands;

public record DeleteUserCommand(
    Long userId
) {
    public DeleteUserCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number.");
        }
    }
}
