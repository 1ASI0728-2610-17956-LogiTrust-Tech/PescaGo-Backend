package pe.upc.pescagobackend.receipt.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReceiptSensitiveDataSanitizerTest {

    @Test
    void sanitizeCvvAlwaysReturnsNotStored() {
        assertThat(ReceiptSensitiveDataSanitizer.sanitizeCvv("123"))
                .isEqualTo(ReceiptSensitiveDataSanitizer.CVV_NOT_STORED);
        assertThat(ReceiptSensitiveDataSanitizer.sanitizeCvv("NOT_STORED"))
                .isEqualTo(ReceiptSensitiveDataSanitizer.CVV_NOT_STORED);
        assertThat(ReceiptSensitiveDataSanitizer.sanitizeCvv(null))
                .isEqualTo(ReceiptSensitiveDataSanitizer.CVV_NOT_STORED);
    }

    @Test
    void sanitizeCardNumberMasksFullPanKeepingLastFour() {
        assertThat(ReceiptSensitiveDataSanitizer.sanitizeCardNumber("4111111111111111"))
                .isEqualTo("CARD-****-1111");
        assertThat(ReceiptSensitiveDataSanitizer.sanitizeCardNumber("4111 1111 1111 1234"))
                .isEqualTo("CARD-****-1234");
    }

    @Test
    void sanitizeCardNumberPreservesWalletTokens() {
        assertThat(ReceiptSensitiveDataSanitizer.sanitizeCardNumber("WALLET-YAPE"))
                .isEqualTo("WALLET-YAPE");
        assertThat(ReceiptSensitiveDataSanitizer.sanitizeCardNumber("wallet-plin"))
                .isEqualTo("WALLET-PLIN");
    }

    @Test
    void sanitizeCardNumberKeepsAlreadyMaskedValues() {
        assertThat(ReceiptSensitiveDataSanitizer.sanitizeCardNumber("CARD-****-9876"))
                .isEqualTo("CARD-****-9876");
    }
}
