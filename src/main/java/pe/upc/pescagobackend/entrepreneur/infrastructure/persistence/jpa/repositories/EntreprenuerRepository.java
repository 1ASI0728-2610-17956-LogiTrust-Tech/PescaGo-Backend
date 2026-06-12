package pe.upc.pescagobackend.entrepreneur.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.upc.pescagobackend.entrepreneur.domain.model.aggregates.Entreprenuer;

import java.util.Optional;

public interface EntreprenuerRepository extends JpaRepository<Entreprenuer, Long> {
    Optional<Entreprenuer>findByUserId(Long id);
}
