package pe.upc.pescagobackend.receipt.interfaces.rest.resources;

public record CreateReceiptResource(
        Long requestId,
        String holderName,
        String cardNumber,
        String expiryDate,
        String cvv,
        String paymentDate
) {
    public CreateReceiptResource {
        if (requestId == null || requestId <= 0) {
            throw new IllegalArgumentException("Request ID must be a positive number.");
        }
        if (holderName == null || holderName.isBlank()) {
            throw new IllegalArgumentException("Holder name cannot be null or blank.");
        }
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new IllegalArgumentException("Card number cannot be null or blank.");
        }
        if (expiryDate == null || expiryDate.isBlank()) {
            throw new IllegalArgumentException("Expiry date cannot be null or blank.");
        }
        if (cvv == null || cvv.isBlank()) {
            throw new IllegalArgumentException("CVV cannot be null or blank.");
        }
        if (paymentDate == null || paymentDate.isBlank()) {
            throw new IllegalArgumentException("Payment date cannot be null or blank.");
        }

    }
}
