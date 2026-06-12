package pe.upc.pescagobackend.receipt.domain.model.queries;

public record GetReceiptByIdQuery(
    Long receiptId
) {
    public GetReceiptByIdQuery {
        if (receiptId == null || receiptId <= 0) {
            throw new IllegalArgumentException("Receipt ID must be a positive number.");
        }
    }
}
