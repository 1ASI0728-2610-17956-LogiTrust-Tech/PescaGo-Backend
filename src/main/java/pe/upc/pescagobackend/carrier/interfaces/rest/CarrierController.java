package pe.upc.pescagobackend.carrier.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pescagobackend.carrier.application.internal.commandservices.CarrierCommandServiceImpl;
import pe.upc.pescagobackend.carrier.application.internal.queryservices.CarrierQueryServiceImpl;
import pe.upc.pescagobackend.carrier.domain.model.aggregates.Carrier;
import pe.upc.pescagobackend.carrier.domain.model.commands.DeleteCarrierCommand;
import pe.upc.pescagobackend.carrier.domain.model.queries.GetCarrierByIdQuery;
import pe.upc.pescagobackend.carrier.domain.model.queries.GetCarriersQuery;
import pe.upc.pescagobackend.carrier.interfaces.rest.resources.CarrierResource;
import pe.upc.pescagobackend.carrier.interfaces.rest.resources.CreateCarrierResource;
import pe.upc.pescagobackend.carrier.interfaces.rest.transform.CarrierResourceFromEntityAssembler;
import pe.upc.pescagobackend.carrier.interfaces.rest.transform.CreateCarrierCommandFromResourceAssembler;
import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.domain.model.commands.DeleteUserCommand;
import pe.upc.pescagobackend.iam.domain.model.queries.GetUserByIdQuery;
import pe.upc.pescagobackend.iam.domain.services.UserCommandService;
import pe.upc.pescagobackend.iam.domain.services.UserQueryService;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.CreateUserResource;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.UserResource;
import pe.upc.pescagobackend.iam.interfaces.rest.transform.CreateUserCommandFromResourceAssembler;
import pe.upc.pescagobackend.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/carriers", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Carrier", description = "Available Carrier Endpoints")
public class CarrierController {
    private final CarrierCommandServiceImpl carrierCommandService;
    private final CarrierQueryServiceImpl carrierQueryService;

    public CarrierController(CarrierCommandServiceImpl carrierCommandService, CarrierQueryServiceImpl carrierQueryService) {
        this.carrierCommandService = carrierCommandService;
        this.carrierQueryService = carrierQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a Carrier", description = "Create a Carrier")
    public ResponseEntity<Carrier> createCarrier(@RequestBody CreateCarrierResource resource) {
        try {
            var createCarrierCommand = CreateCarrierCommandFromResourceAssembler.toCommandFromResource(resource);
            var createdCarrier = carrierCommandService.handle(createCarrierCommand)
                    .orElseThrow(() -> new IllegalArgumentException("Error creating Carrier"));
            return ResponseEntity.status(201).body(createdCarrier);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @DeleteMapping("{userId}")
    @Operation(summary = "Delete a Carrier", description = "Delete a Carrier by id")
    public void deleteUser(@PathVariable Long userId) {
        var deleteCarrierCommand = new DeleteCarrierCommand(userId);
        carrierCommandService.handle(deleteCarrierCommand);
    }

    @GetMapping("{userId}")
    @Operation(summary = "Get Carrier by id", description = "Get Carrier by id")
    public ResponseEntity<CarrierResource> getUser(@PathVariable Long userId) {
        var query = new GetCarrierByIdQuery(userId);
        var carrierOptional = carrierQueryService.handle(query);
        if (carrierOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var carrierResource = CarrierResourceFromEntityAssembler.toResourceFromEntity(carrierOptional.get());
        return ResponseEntity.ok(carrierResource);
    }

    @GetMapping
    @Operation(summary = "Get all Carriers", description = "Get all Carriers")
    public ResponseEntity<List<CarrierResource>> getAllCarriers() {
        var query = new GetCarriersQuery();
        var carriersOptional = carrierQueryService.handle(query);
        if (carriersOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var carrierResources = carriersOptional.get().stream()
                .map(CarrierResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(carrierResources);
    }

}
