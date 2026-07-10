package pe.upc.pescagobackend.receipt.domain.model;

import java.util.Locale;

/**
 * Sanitizes payment instrument fields for legacy V1 receipts.
 * Keeps the existing column/API shape while avoiding storage and exposure of full PAN/CVV.
 */
public final class ReceiptSensitiveDataSanitizer {

    public static final String CVV_NOT_STORED = "NOT_STORED";
    public static final String WALLET_YAPE = "WALLET-YAPE";
    public static final String WALLET_PLIN = "WALLET-PLIN";

    private ReceiptSensitiveDataSanitizer() {
    }

    public static String sanitizeCvv(String ignoredCvv) {
        return CVV_NOT_STORED;
    }

    public static String sanitizeCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isBlank()) {
            return cardNumber;
        }

        String trimmed = cardNumber.trim();
        String upper = trimmed.toUpperCase(Locale.ROOT);

        if (WALLET_YAPE.equals(upper) || WALLET_PLIN.equals(upper)) {
            return upper;
        }

        if (upper.startsWith("CARD-****-")) {
            return upper;
        }

        String digits = trimmed.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            return "CARD-****-XXXX";
        }

        String lastFour = digits.length() <= 4
                ? digits
                : digits.substring(digits.length() - 4);
        return "CARD-****-" + lastFour;
    }
}
