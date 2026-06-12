package pe.upc.pescagobackend.receipt.interfaces.rest.transform;

import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;
import pe.upc.pescagobackend.receipt.interfaces.rest.resources.ReceiptResource;

public class ReceiptResourceFromEntityAssembler {
    public static ReceiptResource toResourceFromEntity(Receipt entity) {
        return new ReceiptResource(
            entity.getId(),
            entity.getRequestId(),
            entity.getHolderName(),
            entity.getCardNumber(),
            entity.getExpiryDate(),
            entity.getCvv(),
            entity.getPaymentDate()
        );
    }
}
