package pe.upc.pescagobackend.fleet.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.upc.pescagobackend.fleet.domain.model.aggregates.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByIdAndCarrierId(Long id, Long carrierId);

    List<Vehicle> findByCarrierIdAndActiveTrueOrderByIdAsc(Long carrierId);

    List<Vehicle> findByCarrierIdOrderByIdAsc(Long carrierId);

    boolean existsByPlate(String plate);

    boolean existsByPlateAndIdNot(String plate, Long id);
}
