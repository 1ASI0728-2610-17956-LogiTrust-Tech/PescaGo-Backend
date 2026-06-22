package pe.upc.pescagobackend.fleet.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.upc.pescagobackend.fleet.application.internal.services.FleetCarrierContextResolver;
import pe.upc.pescagobackend.fleet.domain.exceptions.DuplicatePlateException;
import pe.upc.pescagobackend.fleet.domain.model.commands.ActivateVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.commands.DeactivateVehicleCommand;
import pe.upc.pescagobackend.fleet.domain.model.queries.GetOwnVehicleByIdQuery;
import pe.upc.pescagobackend.fleet.domain.model.queries.GetOwnVehiclesQuery;
import pe.upc.pescagobackend.fleet.domain.services.VehicleCommandService;
import pe.upc.pescagobackend.fleet.domain.services.VehicleQueryService;
import pe.upc.pescagobackend.fleet.interfaces.rest.resources.RegisterVehicleResource;
import pe.upc.pescagobackend.fleet.interfaces.rest.resources.UpdateVehicleResource;
import pe.upc.pescagobackend.fleet.interfaces.rest.resources.VehicleResource;
import pe.upc.pescagobackend.fleet.interfaces.rest.transform.RegisterVehicleCommandFromResourceAssembler;
import pe.upc.pescagobackend.fleet.interfaces.rest.transform.UpdateVehicleCommandFromResourceAssembler;
import pe.upc.pescagobackend.fleet.interfaces.rest.transform.VehicleResourceFromEntityAssembler;
import pe.upc.pescagobackend.shared.domain.model.enums.Role;
import pe.upc.pescagobackend.shared.infrastructure.security.AuthenticatedUser;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v2/fleet/vehicles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Fleet v2", description = "Carrier-owned fleet vehicle management for PescaGo v2")
public class FleetVehicleV2Controller {

    private final VehicleCommandService vehicleCommandService;
    private final VehicleQueryService vehicleQueryService;
    private final FleetCarrierContextResolver fleetCarrierContextResolver;

    public FleetVehicleV2Controller(
            VehicleCommandService vehicleCommandService,
            VehicleQueryService vehicleQueryService,
            FleetCarrierContextResolver fleetCarrierContextResolver
    ) {
        this.vehicleCommandService = vehicleCommandService;
        this.vehicleQueryService = vehicleQueryService;
        this.fleetCarrierContextResolver = fleetCarrierContextResolver;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a fleet vehicle for the authenticated carrier")
    public ResponseEntity<VehicleResource> registerVehicle(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody RegisterVehicleResource resource
    ) {
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ensureCarrierRole(authenticatedUser);
        Long carrierId = resolveCarrierId(authenticatedUser);

        var command = RegisterVehicleCommandFromResourceAssembler.toCommandFromResource(resource, carrierId);
        var vehicle = vehicleCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }

    @GetMapping
    @Operation(summary = "List fleet vehicles owned by the authenticated carrier")
    public ResponseEntity<List<VehicleResource>> listOwnVehicles(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestParam(defaultValue = "false") boolean includeInactive
    ) {
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ensureCarrierRole(authenticatedUser);
        Long carrierId = resolveCarrierId(authenticatedUser);

        var vehicles = vehicleQueryService.handle(new GetOwnVehiclesQuery(carrierId, includeInactive))
                .stream()
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a fleet vehicle owned by the authenticated carrier")
    public ResponseEntity<VehicleResource> getOwnVehicleById(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id
    ) {
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ensureCarrierRole(authenticatedUser);
        Long carrierId = resolveCarrierId(authenticatedUser);

        return vehicleQueryService.handle(new GetOwnVehicleByIdQuery(id, carrierId))
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Replace mutable fields of a fleet vehicle owned by the authenticated carrier")
    public ResponseEntity<VehicleResource> updateOwnVehicle(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleResource resource
    ) {
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ensureCarrierRole(authenticatedUser);
        Long carrierId = resolveCarrierId(authenticatedUser);

        var command = UpdateVehicleCommandFromResourceAssembler.toCommandFromResource(id, carrierId, resource);
        return vehicleCommandService.handle(command)
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a fleet vehicle owned by the authenticated carrier")
    public ResponseEntity<VehicleResource> activateOwnVehicle(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id
    ) {
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ensureCarrierRole(authenticatedUser);
        Long carrierId = resolveCarrierId(authenticatedUser);

        return vehicleCommandService.handle(new ActivateVehicleCommand(id, carrierId))
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a fleet vehicle owned by the authenticated carrier")
    public ResponseEntity<VehicleResource> deactivateOwnVehicle(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id
    ) {
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ensureCarrierRole(authenticatedUser);
        Long carrierId = resolveCarrierId(authenticatedUser);

        return vehicleCommandService.handle(new DeactivateVehicleCommand(id, carrierId))
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private void ensureCarrierRole(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser.getCanonicalRole() != Role.CARRIER) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private Long resolveCarrierId(AuthenticatedUser authenticatedUser) {
        return fleetCarrierContextResolver.resolveCarrierId(authenticatedUser)
                .orElseThrow(() -> new CarrierProfileNotFoundException());
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

    @ExceptionHandler(CarrierProfileNotFoundException.class)
    public ResponseEntity<Void> handleCarrierProfileNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(IllegalArgumentException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
        problemDetail.setTitle("Bad Request");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(DuplicatePlateException.class)
    public ResponseEntity<ProblemDetail> handleDuplicatePlate() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "Vehicle plate already exists"
        );
        problemDetail.setTitle("Conflict");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    private static final class CarrierProfileNotFoundException extends RuntimeException {
    }
}
