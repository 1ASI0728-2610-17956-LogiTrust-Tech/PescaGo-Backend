package pe.upc.pescagobackend.receipt.interfaces.rest.transform;

import pe.upc.pescagobackend.receipt.domain.model.commands.CreateReceiptCommand;
import pe.upc.pescagobackend.receipt.interfaces.rest.resources.CreateReceiptResource;

public class CreateReceiptCommandFromResourceAssembler {
    public static CreateReceiptCommand toCommandFromResource(CreateReceiptResource resource) {
        return new CreateReceiptCommand(
                resource.requestId(),
                resource.holderName(),
                resource.cardNumber(),
                resource.expiryDate(),
                resource.cvv(),
                resource.paymentDate()
        );
    }
}
