package pe.upc.pescagobackend.fleet.application.internal.commandservices;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.fleet.application.internal.persistence.FleetPersistenceConstraintClassifier;
import pe.upc.pescagobackend.fleet.domain.exceptions.DuplicatePlateException;
import pe.upc.pescagobackend.fleet.domain.model.aggregates.Vehicle;
import pe.upc.pescagobackend.fleet.domain.model.commands.ActivateVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.commands.DeactivateVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.commands.RegisterVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.commands.UpdateVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.services.VehicleCommandService;
import pe.upc.pescagobackend.fleet.infrastructure.persistence.jpa.repositories.VehicleRepository;

import java.util.Optional;

@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle handle(RegisterVehicleCommand command) {
        ensurePlateAvailable(command.plate(), null);
        var vehicle = new Vehicle(command);
        return saveVehicle(vehicle, command.plate());
    }

    @Override
    public Optional<Vehicle> handle(UpdateVehicleCommand command) {
        return vehicleRepository.findByIdAndCarrierId(command.vehicleId(), command.carrierId())
                .map(vehicle -> {
                    ensurePlateAvailable(command.plate(), command.vehicleId());
                    vehicle.update(command);
                    return saveVehicle(vehicle, command.plate());
                });
    }

    @Override
    public Optional<Vehicle> handle(ActivateVehicleCommand command) {
        return vehicleRepository.findByIdAndCarrierId(command.vehicleId(), command.carrierId())
                .map(vehicle -> {
                    vehicle.activate();
                    return vehicleRepository.save(vehicle);
                });
    }

    @Override
    public Optional<Vehicle> handle(DeactivateVehicleCommand command) {
        return vehicleRepository.findByIdAndCarrierId(command.vehicleId(), command.carrierId())
                .map(vehicle -> {
                    vehicle.deactivate();
                    return vehicleRepository.save(vehicle);
                });
    }

    private void ensurePlateAvailable(String plate, Long vehicleId) {
        boolean duplicate = vehicleId == null
                ? vehicleRepository.existsByPlate(plate)
                : vehicleRepository.existsByPlateAndIdNot(plate, vehicleId);
        if (duplicate) {
            throw new DuplicatePlateException(plate);
        }
    }

    private Vehicle saveVehicle(Vehicle vehicle, String plate) {
        try {
            return vehicleRepository.save(vehicle);
        } catch (DataIntegrityViolationException ex) {
            if (FleetPersistenceConstraintClassifier.isUniquePlateViolation(ex)) {
                throw new DuplicatePlateException(plate);
            }
            throw ex;
        }
    }
}
