package pe.upc.pescagobackend.receipt.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;
import pe.upc.pescagobackend.receipt.domain.model.queries.GetReceiptByIdQuery;
import pe.upc.pescagobackend.receipt.domain.services.ReceiptQueryService;
import pe.upc.pescagobackend.receipt.infrastructure.persistence.jpa.repositories.ReceiptRepository;

import java.util.Optional;

@Service
public class ReceiptQueryServiceImpl implements ReceiptQueryService {
    private final ReceiptRepository receiptRepository;

    public ReceiptQueryServiceImpl(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public Optional<Receipt> handle(GetReceiptByIdQuery query){
        return receiptRepository.findById(query.receiptId());
    }


}
