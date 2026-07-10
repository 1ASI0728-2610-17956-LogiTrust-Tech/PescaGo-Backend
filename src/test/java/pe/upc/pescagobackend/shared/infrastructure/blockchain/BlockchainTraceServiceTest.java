package pe.upc.pescagobackend.shared.infrastructure.blockchain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlockchainTraceServiceTest {

    @Mock
    private BlockchainProperties properties;

    @Mock
    private MinichainTransactionClient transactionClient;

    @InjectMocks
    private BlockchainTraceService blockchainTraceService;

    @Test
    void recordEventDoesNothingWhenBlockchainDisabled() {
        when(properties.enabled()).thenReturn(false);

        blockchainTraceService.recordEvent(
                BlockchainEventType.REQUEST_CREATED,
                "request:1|carrier:2|entrepreneur:3",
                1L
        );

        verifyNoInteractions(transactionClient);
    }

    @Test
    void recordEventPostsMinichainPayloadWhenEnabled() {
        when(properties.enabled()).thenReturn(true);
        when(properties.baseUrl()).thenReturn("http://localhost:3001");

        blockchainTraceService.recordEvent(
                BlockchainEventType.REQUEST_CREATED,
                "request:42|carrier:7|entrepreneur:3",
                42L
        );

        verify(transactionClient).createTransaction(
                "http://localhost:3001",
                "PESCAGO:REQUEST_CREATED",
                "request:42|carrier:7|entrepreneur:3",
                42L
        );
    }

    @Test
    void recordEventDoesNotPropagateHttpFailures() {
        when(properties.enabled()).thenReturn(true);
        when(properties.baseUrl()).thenReturn("http://localhost:3001");
        doThrow(new RuntimeException("connection refused"))
                .when(transactionClient)
                .createTransaction(
                        "http://localhost:3001",
                        "PESCAGO:REQUEST_CREATED",
                        "request:1|carrier:1|entrepreneur:1",
                        1L
                );

        assertThatCode(() -> blockchainTraceService.recordEvent(
                BlockchainEventType.REQUEST_CREATED,
                "request:1|carrier:1|entrepreneur:1",
                1L
        )).doesNotThrowAnyException();
    }
}
