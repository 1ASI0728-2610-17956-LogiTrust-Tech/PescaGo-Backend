package pe.upc.pescagobackend.hiredService.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pescagobackend.hiredService.domain.model.commands.DeleteHiredServiceCommand;
import pe.upc.pescagobackend.hiredService.domain.model.queries.GetHiredServiceByIdQuery;
import pe.upc.pescagobackend.hiredService.domain.model.queries.GetHiredServicesByCarrierIdQuery;
import pe.upc.pescagobackend.hiredService.domain.model.queries.GetHiredServicesByEntrepreneurIdQuery;
import pe.upc.pescagobackend.hiredService.domain.services.HiredServiceCommandService;
import pe.upc.pescagobackend.hiredService.domain.services.HiredServiceQueryService;
import pe.upc.pescagobackend.hiredService.interfaces.rest.resources.CreateHiredServiceResource;
import pe.upc.pescagobackend.hiredService.interfaces.rest.resources.HiredServiceResource;
import pe.upc.pescagobackend.hiredService.interfaces.rest.resources.UpdateHiredServiceResource;
import pe.upc.pescagobackend.hiredService.interfaces.rest.transform.CreateHiredServiceCommandFromResourceAssembler;
import pe.upc.pescagobackend.hiredService.interfaces.rest.transform.HiredServiceResourceFromEntityAssembler;
import pe.upc.pescagobackend.hiredService.interfaces.rest.transform.UpdateHiredServiceCommandFromResourceAssembler;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/hired-services", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Hired Service", description = "Available Hired Service Endpoints")
public class HiredServiceController {
    private final HiredServiceCommandService hiredServiceCommandService;
    private final HiredServiceQueryService hiredServiceQueryService;

    public HiredServiceController(HiredServiceCommandService hiredServiceCommandService, HiredServiceQueryService hiredServiceQueryService) {
        this.hiredServiceCommandService = hiredServiceCommandService;
        this.hiredServiceQueryService = hiredServiceQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a hired service", description = "Create a hired service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hired Service created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public void createHiredService(@RequestBody CreateHiredServiceResource resource) {
        var createHiredServiceCommand = CreateHiredServiceCommandFromResourceAssembler.toCommandFromResource(resource);
        hiredServiceCommandService.handle(createHiredServiceCommand);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get hired service by id", description = "Get hired service by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hired Service found"),
            @ApiResponse(responseCode = "404", description = "Hired Service not found")
    })
    public ResponseEntity<HiredServiceResource> getHiredServiceById(@PathVariable Long id) {
        var getHiredServiceByIdQuery = new GetHiredServiceByIdQuery(id);
        var hiredService = hiredServiceQueryService.handle(getHiredServiceByIdQuery);
        if (hiredService.isEmpty()) return ResponseEntity.notFound().build();
        var hiredServiceEntity = hiredService.get();
        var hiredServiceResource = HiredServiceResourceFromEntityAssembler.toResourceFromEntity(hiredServiceEntity);
        return ResponseEntity.ok(hiredServiceResource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete hired service by id", description = "Delete hired service by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Hired Service deleted"),
            @ApiResponse(responseCode = "404", description = "Hired Service not found")
    })
    public ResponseEntity<Void> deleteHiredServiceById(@PathVariable Long id) {
        var deleteHiredServiceCommand = new DeleteHiredServiceCommand(id);
        hiredServiceCommandService.handle(deleteHiredServiceCommand);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update hired service by id", description = "Update hired service by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hired Service updated"),
            @ApiResponse(responseCode = "404", description = "Hired Service not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Void> updateHiredServiceById(@PathVariable Long id, @RequestBody UpdateHiredServiceResource resource) {
        var updateHiredServiceCommand = UpdateHiredServiceCommandFromResourceAssembler.toCommandFromResource(id, resource);
        hiredServiceCommandService.handle(updateHiredServiceCommand);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/entrepreneur/{entrepreneurId}")
    @Operation(summary = "Get hired services by entrepreneur ID", description = "Get all hired services for a specific entrepreneur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hired Services found"),
            @ApiResponse(responseCode = "404", description = "No Hired Services found for the given entrepreneur ID")
    })
    public ResponseEntity<List<HiredServiceResource>> getHiredServicesByEntrepreneurId(@PathVariable Long entrepreneurId) {
        var query = new GetHiredServicesByEntrepreneurIdQuery(entrepreneurId);
        var hiredServices = hiredServiceQueryService.handle(query);
        if (hiredServices.isEmpty()) return ResponseEntity.notFound().build();
        var resources = hiredServices.stream()
                .map(HiredServiceResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/carrier/{carrierId}")
    @Operation(summary = "Get hired services by carrier ID", description = "Get all hired services for a specific carrier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hired Services found"),
            @ApiResponse(responseCode = "404", description = "No Hired Services found for the given carrier ID")
    })
    public ResponseEntity<List<HiredServiceResource>> getHiredServicesByCarrierId(@PathVariable Long carrierId) {
        var query = new GetHiredServicesByCarrierIdQuery(carrierId);
        var hiredServices = hiredServiceQueryService.handle(query);
        if (hiredServices.isEmpty()) return ResponseEntity.notFound().build();
        var resources = hiredServices.stream()
                .map(HiredServiceResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

}
