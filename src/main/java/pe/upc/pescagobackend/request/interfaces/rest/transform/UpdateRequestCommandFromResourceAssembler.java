package pe.upc.pescagobackend.request.interfaces.rest.transform;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pe.upc.pescagobackend.request.domain.model.commands.UpdateRequestCommand;
import pe.upc.pescagobackend.request.interfaces.rest.resources.UpdateRequestResource;
import pe.upc.pescagobackend.shared.application.LegacyStatusTranslator;

public class UpdateRequestCommandFromResourceAssembler {
    public static UpdateRequestCommand toCommandFromResource (Long id, UpdateRequestResource resource) {
        return new UpdateRequestCommand(
                id,
                resource.entrepreneurId(),
                resource.entrepreneurName(),
                resource.carrierId(),
                resource.carrierName(),
                resource.packageDescription(),
                resource.quantity(),
                resource.weightTotal(),
                resource.pickupLocation(),
                resource.deliveryLocation(),
                resource.pickupDateTime(),
                resource.price(),
                canonicalizeRequestStatus(resource.status()),
                resource.dimensions()
        );
    }

    private static String canonicalizeRequestStatus(String status) {
        try {
            return LegacyStatusTranslator.canonicalizeRequestStatusForPersistence(status);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
