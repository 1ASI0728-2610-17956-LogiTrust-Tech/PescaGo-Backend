package pe.upc.pescagobackend.receipt.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;
import pe.upc.pescagobackend.receipt.domain.model.commands.CreateReceiptCommand;
import pe.upc.pescagobackend.receipt.domain.model.commands.DeleteReceiptCommand;
import pe.upc.pescagobackend.receipt.domain.services.ReceiptCommandService;
import pe.upc.pescagobackend.receipt.infrastructure.persistence.jpa.repositories.ReceiptRepository;

import java.util.Optional;

@Service
public class ReceiptCommandServiceImpl implements ReceiptCommandService {
    private final ReceiptRepository receiptRepository;

    public ReceiptCommandServiceImpl(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public Optional<Receipt> handle(CreateReceiptCommand command) {
        var receipt = new Receipt(command);
        try {
            receiptRepository.save(receipt);
            return Optional.of(receipt);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving the receipt: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteReceiptCommand command) {
       if (!receiptRepository.existsById(command.receiptId())) {
            throw new IllegalArgumentException("Receipt with id %s not found".formatted(command.receiptId()));
        }
        try {
            receiptRepository.deleteById(command.receiptId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting Receipt: %s".formatted(e.getMessage()));
        }
    }
}
