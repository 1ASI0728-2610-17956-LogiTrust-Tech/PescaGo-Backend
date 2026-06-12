package pe.upc.pescagobackend.request.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pescagobackend.request.domain.model.aggregates.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findById(Long id);
    List<Request>findByCarrierId(Long carrierId);
    List<Request> findByEntrepreneurId(Long entrepreneurId);
}
