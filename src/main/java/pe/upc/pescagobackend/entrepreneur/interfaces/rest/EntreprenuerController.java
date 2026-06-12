package pe.upc.pescagobackend.entrepreneur.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pescagobackend.entrepreneur.application.internal.commandservices.EntreprenuerCommandServiceImpl;
import pe.upc.pescagobackend.entrepreneur.application.internal.queryservices.EntreprenuerQueryServiceImpl;
import pe.upc.pescagobackend.entrepreneur.domain.model.aggregates.Entreprenuer;
import pe.upc.pescagobackend.entrepreneur.domain.model.commands.DeleteEntreprenuerCommand;
import pe.upc.pescagobackend.entrepreneur.domain.model.queries.GetEntreprenuerByIdQuery;
import pe.upc.pescagobackend.entrepreneur.interfaces.rest.resources.CreateEntreprenuerResource;
import pe.upc.pescagobackend.entrepreneur.interfaces.rest.resources.EntreprenuerResource;
import pe.upc.pescagobackend.entrepreneur.interfaces.rest.transform.CreateEntreprenuerCommandFromResourceAssembler;
import pe.upc.pescagobackend.entrepreneur.interfaces.rest.transform.EntreprenuerResourceFromEntityAssembler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/entreprenuers", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Entreprenuer", description = "Available Entreprenuer Endpoints")
public class EntreprenuerController {
    private final EntreprenuerCommandServiceImpl entreprenuerCommandService;
    private final EntreprenuerQueryServiceImpl entreprenuerQueryService;

    public EntreprenuerController(EntreprenuerCommandServiceImpl entreprenuerCommandService, EntreprenuerQueryServiceImpl entreprenuerQueryService) {
        this.entreprenuerCommandService = entreprenuerCommandService;
        this.entreprenuerQueryService = entreprenuerQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a Entreprenuer", description = "Create a Entreprenuer")
    public ResponseEntity<Entreprenuer> createCarrier(@RequestBody CreateEntreprenuerResource resource) {
        try {
            var createEntreprenuerCommand = CreateEntreprenuerCommandFromResourceAssembler.toCommandFromResource(resource);
            var createdEntreprenuer = entreprenuerCommandService.handle(createEntreprenuerCommand)
                    .orElseThrow(() -> new IllegalArgumentException("Error creating Entreprenuer"));
            return ResponseEntity.status(201).body(createdEntreprenuer);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @DeleteMapping("{userId}")
    @Operation(summary = "Delete a Entreprenuer", description = "Delete a Entreprenuer by id")
    public void deleteUser(@PathVariable Long userId) {
        var deleteEntreprenuerCommand = new DeleteEntreprenuerCommand(userId);
        entreprenuerCommandService.handle(deleteEntreprenuerCommand);
    }

    @GetMapping("{userId}")
    @Operation(summary = "Get Entreprenuer by id", description = "Get Entreprenuer by id")
    public ResponseEntity<EntreprenuerResource> getUser(@PathVariable Long userId) {
        var query = new GetEntreprenuerByIdQuery(userId);
        var entreprenuerOptional = entreprenuerQueryService.handle(query);
        if (entreprenuerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var entreprenuerResource = EntreprenuerResourceFromEntityAssembler.toResourceFromEntity(entreprenuerOptional.get());
        return ResponseEntity.ok(entreprenuerResource);
    }

}
