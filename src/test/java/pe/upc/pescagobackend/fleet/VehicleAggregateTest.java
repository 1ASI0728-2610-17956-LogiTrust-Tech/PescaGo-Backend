package pe.upc.pescagobackend.fleet;

import org.junit.jupiter.api.Test;
import pe.upc.pescagobackend.fleet.domain.model.aggregates.Vehicle;
import pe.upc.pescagobackend.fleet.domain.model.commands.RegisterVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.commands.UpdateVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleAvailabilityStatus;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleAggregateTest {

    @Test
    void updateReplacesMutableFieldsWithoutChangingActive() {
        var vehicle = new Vehicle(new RegisterVehicleCommand(
                1L,
                "abc-123",
                VehicleType.TRUCK,
                new BigDecimal("1500.50"),
                new BigDecimal("12.500"),
                true,
                VehicleAvailabilityStatus.AVAILABLE
        ));
        vehicle.deactivate();

        vehicle.update(new UpdateVehicleCommand(
                99L,
                1L,
                "xyz-999",
                VehicleType.VAN,
                new BigDecimal("800.00"),
                null,
                false,
                VehicleAvailabilityStatus.UNAVAILABLE
        ));

        assertThat(vehicle.getPlate()).isEqualTo("XYZ-999");
        assertThat(vehicle.getVehicleType()).isEqualTo(VehicleType.VAN);
        assertThat(vehicle.getMaxWeightKg()).isEqualByComparingTo("800.00");
        assertThat(vehicle.getMaxVolumeM3()).isNull();
        assertThat(vehicle.isRefrigerated()).isFalse();
        assertThat(vehicle.getAvailabilityStatus()).isEqualTo(VehicleAvailabilityStatus.UNAVAILABLE);
        assertThat(vehicle.isActive()).isFalse();
    }
}
