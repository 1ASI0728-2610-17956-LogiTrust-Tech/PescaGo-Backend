package pe.upc.pescagobackend.fleet.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.fleet.domain.model.aggregates.Vehicle;
import pe.upc.pescagobackend.fleet.domain.model.queries.GetOwnVehicleByIdQuery;
import pe.upc.pescagobackend.fleet.domain.model.queries.GetOwnVehiclesQuery;
import pe.upc.pescagobackend.fleet.domain.services.VehicleQueryService;
import pe.upc.pescagobackend.fleet.infrastructure.persistence.jpa.repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> handle(GetOwnVehiclesQuery query) {
        if (query.includeInactive()) {
            return vehicleRepository.findByCarrierIdOrderByIdAsc(query.carrierId());
        }
        return vehicleRepository.findByCarrierIdAndActiveTrueOrderByIdAsc(query.carrierId());
    }

    @Override
    public Optional<Vehicle> handle(GetOwnVehicleByIdQuery query) {
        return vehicleRepository.findByIdAndCarrierId(query.vehicleId(), query.carrierId());
    }
}
