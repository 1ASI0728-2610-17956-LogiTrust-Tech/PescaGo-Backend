package pe.upc.pescagobackend.fleet.interfaces.rest.transform;

import pe.upc.pescagobackend.fleet.domain.model.aggregates.Vehicle;
import pe.upc.pescagobackend.fleet.interfaces.rest.resources.VehicleResource;

public class VehicleResourceFromEntityAssembler {

    private VehicleResourceFromEntityAssembler() {
    }

    public static VehicleResource toResourceFromEntity(Vehicle entity) {
        return new VehicleResource(
                entity.getId(),
                entity.getPlate(),
                entity.getVehicleType(),
                entity.getMaxWeightKg(),
                entity.getMaxVolumeM3(),
                entity.isRefrigerated(),
                entity.getAvailabilityStatus(),
                entity.isActive()
        );
    }
}
