package pe.upc.pescagobackend.fleet.interfaces.rest.transform;

import pe.upc.pescagobackend.fleet.domain.model.commands.RegisterVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.enums.VehicleAvailabilityStatus;
import pe.upc.pescagobackend.fleet.interfaces.rest.resources.RegisterVehicleResource;

public class RegisterVehicleCommandFromResourceAssembler {

    private RegisterVehicleCommandFromResourceAssembler() {
    }

    public static RegisterVehicleCommand toCommandFromResource(RegisterVehicleResource resource, Long carrierId) {
        VehicleAvailabilityStatus availabilityStatus = resource.availabilityStatus() == null
                ? VehicleAvailabilityStatus.AVAILABLE
                : resource.availabilityStatus();
        return new RegisterVehicleCommand(
                carrierId,
                resource.plate(),
                resource.vehicleType(),
                resource.maxWeightKg(),
                resource.maxVolumeM3(),
                resource.refrigerated(),
                availabilityStatus
        );
    }
}
