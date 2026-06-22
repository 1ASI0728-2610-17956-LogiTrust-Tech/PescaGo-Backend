package pe.upc.pescagobackend.hiredService.interfaces.rest.transform;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pe.upc.pescagobackend.hiredService.domain.model.commands.CreateHiredServiceCommand;
import pe.upc.pescagobackend.hiredService.interfaces.rest.resources.CreateHiredServiceResource;
import pe.upc.pescagobackend.shared.application.LegacyStatusTranslator;

public class CreateHiredServiceCommandFromResourceAssembler {
    public static CreateHiredServiceCommand toCommandFromResource(CreateHiredServiceResource resource) {
        return new CreateHiredServiceCommand(
                resource.requestId(),
                resource.entrepreneurId(),
                resource.entrepreneurName(),
                resource.carrierId(),
                resource.carrierName(),
                resource.packageDescription(),
                resource.pickupDateTime(),
                resource.paymentMethod(),
                canonicalizeExecutionStatus(resource.status()),
                resource.carrierData()
        );
    }

    private static String canonicalizeExecutionStatus(String status) {
        try {
            return LegacyStatusTranslator.canonicalizeExecutionStatusForPersistence(status);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
