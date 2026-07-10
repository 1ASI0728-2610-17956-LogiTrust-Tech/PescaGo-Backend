package pe.upc.pescagobackend.receipt.application.internal.commandservices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;
import pe.upc.pescagobackend.receipt.domain.model.commands.CreateReceiptCommand;
import pe.upc.pescagobackend.receipt.infrastructure.persistence.jpa.repositories.ReceiptRepository;
import pe.upc.pescagobackend.shared.infrastructure.blockchain.BlockchainEventType;
import pe.upc.pescagobackend.shared.infrastructure.blockchain.BlockchainTraceService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptCommandServiceImplBlockchainTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private BlockchainTraceService blockchainTraceService;

    @InjectMocks
    private ReceiptCommandServiceImpl receiptCommandService;

    @Test
    void createReceiptRecordsPaymentRegisteredWithoutSensitiveData() {
        var command = new CreateReceiptCommand(
                42L,
                "Holder Name",
                "4111111111111111",
                "12/30",
                "123",
                "2026-06-21"
        );

        when(receiptRepository.save(any(Receipt.class))).thenAnswer(invocation -> {
            var saved = invocation.getArgument(0, Receipt.class);
            saved.setId(15L);
            return saved;
        });

        var result = receiptCommandService.handle(command);

        assertThat(result).isPresent();
        assertThat(result.get().getCardNumber()).isEqualTo("CARD-****-1111");
        assertThat(result.get().getCvv()).isEqualTo("NOT_STORED");
        verify(blockchainTraceService).recordEvent(
                BlockchainEventType.PAYMENT_REGISTERED,
                "request:42|receipt:15",
                42L
        );
    }
}
