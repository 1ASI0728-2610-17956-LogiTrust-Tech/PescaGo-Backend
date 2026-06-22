package pe.upc.pescagobackend.fleet.interfaces.rest.transform;

import pe.upc.pescagobackend.fleet.domain.model.commands.UpdateVehicleCommand;
import pe.upc.pescagobackend.fleet.interfaces.rest.resources.UpdateVehicleResource;

public class UpdateVehicleCommandFromResourceAssembler {

    private UpdateVehicleCommandFromResourceAssembler() {
    }

    public static UpdateVehicleCommand toCommandFromResource(
            Long vehicleId,
            Long carrierId,
            UpdateVehicleResource resource
    ) {
        return new UpdateVehicleCommand(
                vehicleId,
                carrierId,
                resource.plate(),
                resource.vehicleType(),
                resource.maxWeightKg(),
                resource.maxVolumeM3(),
                resource.refrigerated(),
                resource.availabilityStatus()
        );
    }
}
