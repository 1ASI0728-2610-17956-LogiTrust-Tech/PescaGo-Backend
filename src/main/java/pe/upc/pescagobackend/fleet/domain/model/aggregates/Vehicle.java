package pe.upc.pescagobackend.fleet.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pe.upc.pescagobackend.fleet.domain.model.commands.RegisterVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.commands.UpdateVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleAvailabilityStatus;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleType;
import pe.upc.pescagobackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "fleet_vehicles")
public class Vehicle extends AuditableAbstractAggregateRoot<Vehicle> {

    @Setter
    @Column(nullable = false)
    private Long carrierId;

    @Setter
    @Column(nullable = false, length = 20)
    private String plate;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VehicleType vehicleType;

    @Setter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal maxWeightKg;

    @Setter
    @Column(precision = 10, scale = 3)
    private BigDecimal maxVolumeM3;

    @Setter
    @Column(nullable = false)
    private boolean refrigerated;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleAvailabilityStatus availabilityStatus;

    @Setter
    @Column(nullable = false)
    private boolean active = true;

    protected Vehicle() {
    }

    public Vehicle(RegisterVehicleCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("RegisterVehicleCommand cannot be null.");
        }
        this.carrierId = command.carrierId();
        this.plate = command.plate();
        this.vehicleType = command.vehicleType();
        this.maxWeightKg = command.maxWeightKg();
        this.maxVolumeM3 = command.maxVolumeM3();
        this.refrigerated = command.refrigerated();
        this.availabilityStatus = command.availabilityStatus();
        this.active = true;
    }

    public void update(UpdateVehicleCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("UpdateVehicleCommand cannot be null.");
        }
        this.plate = command.plate();
        this.vehicleType = command.vehicleType();
        this.maxWeightKg = command.maxWeightKg();
        this.maxVolumeM3 = command.maxVolumeM3();
        this.refrigerated = command.refrigerated();
        this.availabilityStatus = command.availabilityStatus();
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
