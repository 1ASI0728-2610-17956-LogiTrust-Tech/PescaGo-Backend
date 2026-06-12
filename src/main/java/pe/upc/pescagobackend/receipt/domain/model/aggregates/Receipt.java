package pe.upc.pescagobackend.receipt.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pe.upc.pescagobackend.carrier.domain.model.commands.CreateCarrierCommand;
import pe.upc.pescagobackend.receipt.domain.model.commands.CreateReceiptCommand;
import pe.upc.pescagobackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Getter
@Entity
public class Receipt extends AuditableAbstractAggregateRoot<Receipt> {
    @Setter
    private Long requestId;
    @Setter
    private String holderName;
    @Setter
    private String cardNumber;
    @Setter
    private String expiryDate;
    @Setter
    private String cvv;
    @Setter
    private String paymentDate;

    public Receipt() {
        this.requestId = null;
        this.holderName = "";
        this.cardNumber = "";
        this.expiryDate = "";
        this.cvv = "";
        this.paymentDate = "";
    }

    public Receipt(CreateReceiptCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("CreateReceiptCommand cannot be null.");
        }
        this.requestId = command.receiptId();
        this.holderName = command.holderName();
        this.cardNumber = command.cardNumber();
        this.expiryDate = command.expiryDate();
        this.cvv = command.cvv();
        this.paymentDate = command.paymentDate();
    }
}
