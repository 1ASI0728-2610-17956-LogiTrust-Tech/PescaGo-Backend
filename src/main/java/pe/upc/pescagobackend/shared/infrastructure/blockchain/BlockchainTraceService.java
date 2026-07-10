package pe.upc.pescagobackend.shared.infrastructure.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BlockchainTraceService {

    private static final Logger log = LoggerFactory.getLogger(BlockchainTraceService.class);

    private final BlockchainProperties properties;
    private final MinichainTransactionClient transactionClient;

    public BlockchainTraceService(
            BlockchainProperties properties,
            MinichainTransactionClient transactionClient
    ) {
        this.properties = properties;
        this.transactionClient = transactionClient;
    }

    public void recordEvent(BlockchainEventType eventType, String businessKey, long referenceAmount) {
        if (!properties.enabled()) {
            return;
        }

        var from = "PESCAGO:" + eventType.name();

        try {
            transactionClient.createTransaction(
                    properties.baseUrl(),
                    from,
                    businessKey,
                    referenceAmount
            );
            log.info("Blockchain event recorded: {} -> {}", from, businessKey);
        } catch (Exception ex) {
            log.warn(
                    "Blockchain trace failed (non-blocking): event={}, businessKey={}, reason={}",
                    from,
                    businessKey,
                    ex.getMessage()
            );
        }
    }
}
