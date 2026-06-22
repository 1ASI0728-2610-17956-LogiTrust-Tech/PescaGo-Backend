package pe.upc.pescagobackend.fleet.domain.services;

import pe.upc.pescagobackend.fleet.domain.model.aggregates.Vehicle;
import pe.upc.pescagobackend.fleet.domain.model.commands.ActivateVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.commands.DeactivateVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.commands.RegisterVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.commands.UpdateVehicleCommand;

import java.util.Optional;

public interface VehicleCommandService {
    Vehicle handle(RegisterVehicleCommand command);

    Optional<Vehicle> handle(UpdateVehicleCommand command);

    Optional<Vehicle> handle(ActivateVehicleCommand command);

    Optional<Vehicle> handle(DeactivateVehicleCommand command);
}
