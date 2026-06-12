package pe.upc.pescagobackend.hiredService.domain.model.commands;

public record DeleteHiredServiceCommand(Long id) {
    public DeleteHiredServiceCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Hired service ID must be a positive number");
        }
    }
}
