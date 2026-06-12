package pe.upc.pescagobackend.receipt.domain.services;

import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;
import pe.upc.pescagobackend.receipt.domain.model.commands.CreateReceiptCommand;
import pe.upc.pescagobackend.receipt.domain.model.commands.DeleteReceiptCommand;

import java.util.Optional;

public interface ReceiptCommandService {
    Optional<Receipt> handle(CreateReceiptCommand command);
    void handle(DeleteReceiptCommand command);
}
