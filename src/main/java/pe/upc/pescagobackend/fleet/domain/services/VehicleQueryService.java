package pe.upc.pescagobackend.fleet.domain.services;

import pe.upc.pescagobackend.fleet.domain.model.aggregates.Vehicle;
import pe.upc.pescagobackend.fleet.domain.model.queries.GetOwnVehicleByIdQuery;
import pe.upc.pescagobackend.fleet.domain.model.queries.GetOwnVehiclesQuery;

import java.util.List;
import java.util.Optional;

public interface VehicleQueryService {
    List<Vehicle> handle(GetOwnVehiclesQuery query);

    Optional<Vehicle> handle(GetOwnVehicleByIdQuery query);
}
