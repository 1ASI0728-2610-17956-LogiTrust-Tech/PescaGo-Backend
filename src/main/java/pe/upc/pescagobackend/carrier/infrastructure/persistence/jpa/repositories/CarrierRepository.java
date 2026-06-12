package pe.upc.pescagobackend.carrier.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.upc.pescagobackend.carrier.domain.model.aggregates.Carrier;

import java.util.List;
import java.util.Optional;

public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    Optional<Carrier>findByUserId(Long id);
    List<Carrier> findAll();
}
