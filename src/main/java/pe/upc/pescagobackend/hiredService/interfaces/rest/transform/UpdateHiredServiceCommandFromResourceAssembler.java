package pe.upc.pescagobackend.hiredService.interfaces.rest.transform;

import pe.upc.pescagobackend.hiredService.domain.model.commands.UpdateHiredServiceCommand;
import pe.upc.pescagobackend.hiredService.interfaces.rest.resources.UpdateHiredServiceResource;

public class UpdateHiredServiceCommandFromResourceAssembler {
    public static UpdateHiredServiceCommand toCommandFromResource(Long id, UpdateHiredServiceResource resource) {
        return new UpdateHiredServiceCommand(
                id,
                resource.requestId(),
                resource.entrepreneurId(),
                resource.entrepreneurName(),
                resource.carrierId(),
                resource.carrierName(),
                resource.packageDescription(),
                resource.pickupDateTime(),
                resource.paymentMethod(),
                resource.status(),
                resource.carrierData()
        );
    }
}
