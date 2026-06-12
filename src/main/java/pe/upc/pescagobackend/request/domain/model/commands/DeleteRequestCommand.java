package pe.upc.pescagobackend.request.domain.model.commands;

public record DeleteRequestCommand(Long id) {
    public DeleteRequestCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Request ID must be a positive number");
        }
    }
}
