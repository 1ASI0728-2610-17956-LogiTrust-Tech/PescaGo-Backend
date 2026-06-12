package pe.upc.pescagobackend.receipt.domain.model.commands;

public record DeleteReceiptCommand(
    Long receiptId
) {
    public DeleteReceiptCommand {
        if (receiptId == null || receiptId <= 0) {
            throw new IllegalArgumentException("Receipt ID must be a positive number.");
        }
    }
}
