package pe.upc.pescagobackend.receipt.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt>findById(Long id);
}
