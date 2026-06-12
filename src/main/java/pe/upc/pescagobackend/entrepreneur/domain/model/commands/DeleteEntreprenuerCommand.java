package pe.upc.pescagobackend.entrepreneur.domain.model.commands;

public record DeleteEntreprenuerCommand(
    Long entreprenuerId
) {
    public DeleteEntreprenuerCommand {
        if (entreprenuerId == null || entreprenuerId <= 0) {
            throw new IllegalArgumentException("Entreprenuer ID must be a positive number.");
        }
    }
}
