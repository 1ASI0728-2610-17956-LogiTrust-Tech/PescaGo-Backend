package pe.upc.pescagobackend.request.interfaces.rest.resources;

public record PayRequestResource(
        String holderName,
        String cardNumber,
        String expiryDate,
        String cvv,
        String paymentDate,
        String paymentMethod
) {
    public PayRequestResource {
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
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Payment method cannot be null or blank.");
        }
    }
}
