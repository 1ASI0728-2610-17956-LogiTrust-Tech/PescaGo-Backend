package pe.upc.pescagobackend.carrier.domain.model.commands;

public record DeleteCarrierCommand(
    Long carrierId
) {
    public DeleteCarrierCommand {
        if (carrierId == null || carrierId <= 0) {
            throw new IllegalArgumentException("Carrier ID must be a positive number.");
        }
    }
}
