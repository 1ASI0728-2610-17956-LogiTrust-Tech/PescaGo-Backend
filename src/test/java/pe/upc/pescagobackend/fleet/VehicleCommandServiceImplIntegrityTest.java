package pe.upc.pescagobackend.fleet;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import pe.upc.pescagobackend.fleet.application.internal.commandservices.VehicleCommandServiceImpl;
import pe.upc.pescagobackend.fleet.domain.exceptions.DuplicatePlateException;
import pe.upc.pescagobackend.fleet.domain.model.aggregates.Vehicle;
import pe.upc.pescagobackend.fleet.domain.model.commands.RegisterVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleAvailabilityStatus;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleType;
import pe.upc.pescagobackend.fleet.infrastructure.persistence.jpa.repositories.VehicleRepository;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleCommandServiceImplIntegrityTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleCommandServiceImpl vehicleCommandService;

    @Test
    void saveVehicleConvertsUniquePlateIntegrityViolationToDuplicatePlateException() {
        var command = registerCommand("ABC-123");
        when(vehicleRepository.existsByPlate("ABC-123")).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenThrow(uniquePlateIntegrityViolation());

        assertThatThrownBy(() -> vehicleCommandService.handle(command))
                .isInstanceOf(DuplicatePlateException.class);
    }

    @Test
    void saveVehicleRethrowsNonPlateIntegrityViolationWithoutMappingToDuplicatePlate() {
        var command = registerCommand("XYZ-999");
        when(vehicleRepository.existsByPlate("XYZ-999")).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenThrow(foreignKeyIntegrityViolation());

        assertThatThrownBy(() -> vehicleCommandService.handle(command))
                .isInstanceOf(DataIntegrityViolationException.class)
                .isNotInstanceOf(DuplicatePlateException.class);
    }

    private RegisterVehicleCommand registerCommand(String plate) {
        return new RegisterVehicleCommand(
                1L,
                plate,
                VehicleType.TRUCK,
                new BigDecimal("1500.00"),
                null,
                true,
                VehicleAvailabilityStatus.AVAILABLE
        );
    }

    private DataIntegrityViolationException uniquePlateIntegrityViolation() {
        var sqlException = new SQLException(
                "duplicate key value violates unique constraint \"uq_fleet_vehicles_plate\"",
                "23505"
        );
        var hibernateException = new ConstraintViolationException(
                "could not execute statement",
                sqlException,
                "uq_fleet_vehicles_plate"
        );
        return new DataIntegrityViolationException("save failed", hibernateException);
    }

    private DataIntegrityViolationException foreignKeyIntegrityViolation() {
        var sqlException = new SQLException(
                "insert or update on table \"fleet_vehicles\" violates foreign key constraint \"fk_fleet_vehicles_carrier\"",
                "23503"
        );
        var hibernateException = new ConstraintViolationException(
                "could not execute statement",
                sqlException,
                "fk_fleet_vehicles_carrier"
        );
        return new DataIntegrityViolationException("save failed", hibernateException);
    }
}
