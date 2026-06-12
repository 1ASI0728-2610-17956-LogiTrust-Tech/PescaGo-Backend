package pe.upc.pescagobackend.receipt.domain.services;

import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;
import pe.upc.pescagobackend.receipt.domain.model.queries.GetReceiptByIdQuery;

import java.util.Optional;

public interface ReceiptQueryService {
    Optional<Receipt> handle(GetReceiptByIdQuery query);
}
