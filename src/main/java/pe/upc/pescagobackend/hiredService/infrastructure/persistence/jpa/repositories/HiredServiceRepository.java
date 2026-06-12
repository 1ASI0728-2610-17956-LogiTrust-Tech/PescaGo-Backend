package pe.upc.pescagobackend.hiredService.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.upc.pescagobackend.hiredService.domain.model.aggregates.HiredService;

import java.util.List;
import java.util.Optional;

public interface HiredServiceRepository extends JpaRepository<HiredService, Long> {
    Optional<HiredService> findById(Long id);
    List<HiredService> findByEntrepreneurId(Long entrepreneurId);
    List<HiredService> findByCarrierId(Long carrierId);
}
