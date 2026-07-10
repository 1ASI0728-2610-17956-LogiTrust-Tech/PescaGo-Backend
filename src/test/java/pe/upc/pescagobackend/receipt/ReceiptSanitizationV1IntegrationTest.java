package pe.upc.pescagobackend.receipt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.upc.pescagobackend.receipt.domain.model.ReceiptSensitiveDataSanitizer;
import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;
import pe.upc.pescagobackend.receipt.infrastructure.persistence.jpa.repositories.ReceiptRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReceiptSanitizationV1IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Test
    void createCardReceiptPersistsMaskedPanAndNotStoredCvv() throws Exception {
        mockMvc.perform(post("/api/v1/receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "requestId": 101,
                                  "holderName": "Juan Perez",
                                  "cardNumber": "4111111111111111",
                                  "expiryDate": "2028-06",
                                  "cvv": "123",
                                  "paymentDate": "2026-07-10T12:00:00Z"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cardNumber").value("CARD-****-1111"))
                .andExpect(jsonPath("$.cvv").value(ReceiptSensitiveDataSanitizer.CVV_NOT_STORED));

        var saved = receiptRepository.findAll().stream()
                .filter(receipt -> Long.valueOf(101L).equals(receipt.getRequestId()))
                .filter(receipt -> "Juan Perez".equals(receipt.getHolderName()))
                .findFirst()
                .orElseThrow();

        assertThat(saved.getCardNumber()).isEqualTo("CARD-****-1111");
        assertThat(saved.getCvv()).isEqualTo(ReceiptSensitiveDataSanitizer.CVV_NOT_STORED);
    }

    @Test
    void createWalletReceiptPreservesWalletTokenAndNotStoredCvv() throws Exception {
        mockMvc.perform(post("/api/v1/receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "requestId": 102,
                                  "holderName": "Pago simulado Yape",
                                  "cardNumber": "WALLET-YAPE",
                                  "expiryDate": "2026-07",
                                  "cvv": "NOT_STORED",
                                  "paymentDate": "2026-07-10T12:00:00Z"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cardNumber").value("WALLET-YAPE"))
                .andExpect(jsonPath("$.cvv").value(ReceiptSensitiveDataSanitizer.CVV_NOT_STORED));

        mockMvc.perform(post("/api/v1/receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "requestId": 103,
                                  "holderName": "Pago simulado Plin",
                                  "cardNumber": "WALLET-PLIN",
                                  "expiryDate": "2026-07",
                                  "cvv": "999",
                                  "paymentDate": "2026-07-10T12:05:00Z"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cardNumber").value("WALLET-PLIN"))
                .andExpect(jsonPath("$.cvv").value(ReceiptSensitiveDataSanitizer.CVV_NOT_STORED));
    }

    @Test
    void getLegacyReceiptWithSensitiveDataReturnsSanitizedResponse() throws Exception {
        Receipt legacy = new Receipt();
        legacy.setRequestId(201L);
        legacy.setHolderName("Legacy Holder");
        legacy.setCardNumber("5500000000000004");
        legacy.setExpiryDate("2027-01");
        legacy.setCvv("456");
        legacy.setPaymentDate("2025-01-01T00:00:00Z");
        legacy = receiptRepository.save(legacy);

        mockMvc.perform(get("/api/v1/receipts/{id}", legacy.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("CARD-****-0004"))
                .andExpect(jsonPath("$.cvv").value(ReceiptSensitiveDataSanitizer.CVV_NOT_STORED))
                .andExpect(jsonPath("$.holderName").value("Legacy Holder"));

        // DB row remains as stored historically; only API response is sanitized for legacy rows.
        var persisted = receiptRepository.findById(legacy.getId()).orElseThrow();
        assertThat(persisted.getCardNumber()).isEqualTo("5500000000000004");
        assertThat(persisted.getCvv()).isEqualTo("456");
    }
}
