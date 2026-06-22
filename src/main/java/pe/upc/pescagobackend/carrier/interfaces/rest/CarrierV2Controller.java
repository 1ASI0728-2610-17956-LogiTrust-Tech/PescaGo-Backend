package pe.upc.pescagobackend.carrier.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.upc.pescagobackend.carrier.domain.model.queries.GetCarrierByUserIdQuery;
import pe.upc.pescagobackend.carrier.domain.services.CarrierQueryService;
import pe.upc.pescagobackend.carrier.interfaces.rest.resources.CarrierResource;
import pe.upc.pescagobackend.carrier.interfaces.rest.transform.CarrierResourceFromEntityAssembler;
import pe.upc.pescagobackend.shared.domain.model.enums.Role;
import pe.upc.pescagobackend.shared.infrastructure.security.AuthenticatedUser;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v2/carriers", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Carrier v2", description = "Authenticated carrier compatibility endpoints for PescaGo v2")
public class CarrierV2Controller {

    private final CarrierQueryService carrierQueryService;

    public CarrierV2Controller(CarrierQueryService carrierQueryService) {
        this.carrierQueryService = carrierQueryService;
    }

    @GetMapping("/by-user/{userId}")
    @Operation(
            summary = "Get Carrier by user id",
            description = "Returns the carrier profile linked to the given user id. "
                    + "Accessible only for the authenticated user's own id or ADMIN."
    )
    public ResponseEntity<CarrierResource> getCarrierByUserId(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long userId
    ) {
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ensureCanAccessUser(authenticatedUser, userId);

        return carrierQueryService.handle(new GetCarrierByUserIdQuery(userId))
                .map(CarrierResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private void ensureCanAccessUser(AuthenticatedUser authenticatedUser, Long targetUserId) {
        if (authenticatedUser.getCanonicalRole() == Role.ADMIN) {
            return;
        }
        if (!authenticatedUser.getUserId().equals(targetUserId)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "Access denied"
        );
        problemDetail.setTitle("Forbidden");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
    }
}
