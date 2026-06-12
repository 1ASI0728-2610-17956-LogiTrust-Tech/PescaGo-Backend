package pe.upc.pescagobackend.receipt.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pescagobackend.receipt.application.internal.commandservices.ReceiptCommandServiceImpl;
import pe.upc.pescagobackend.receipt.application.internal.queryservices.ReceiptQueryServiceImpl;
import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;
import pe.upc.pescagobackend.receipt.domain.model.commands.DeleteReceiptCommand;
import pe.upc.pescagobackend.receipt.domain.model.queries.GetReceiptByIdQuery;
import pe.upc.pescagobackend.receipt.interfaces.rest.resources.CreateReceiptResource;
import pe.upc.pescagobackend.receipt.interfaces.rest.resources.ReceiptResource;
import pe.upc.pescagobackend.receipt.interfaces.rest.transform.CreateReceiptCommandFromResourceAssembler;
import pe.upc.pescagobackend.receipt.interfaces.rest.transform.ReceiptResourceFromEntityAssembler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/receipts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Receipt", description = "Available Receipt Endpoints")
public class ReceiptController {
    private final ReceiptCommandServiceImpl receiptCommandService;
    private final ReceiptQueryServiceImpl receiptQueryService;

    public ReceiptController(ReceiptCommandServiceImpl receiptCommandService, ReceiptQueryServiceImpl receiptQueryService) {
        this.receiptCommandService = receiptCommandService;
        this.receiptQueryService = receiptQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a Receipt", description = "Create a Receipt")
    public ResponseEntity<Receipt> createCarrier(@RequestBody CreateReceiptResource resource) {
        try {
            var createReceiptCommand = CreateReceiptCommandFromResourceAssembler.toCommandFromResource(resource);
            var createdReceipt = receiptCommandService.handle(createReceiptCommand)
                    .orElseThrow(() -> new IllegalArgumentException("Error creating Carrier"));
            return ResponseEntity.status(201).body(createdReceipt);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @DeleteMapping("{receiptId}")
    @Operation(summary = "Delete a Receipt", description = "Delete a Receipt by id")
    public void deleteUser(@PathVariable Long receiptId) {
        var deleteReceiptCommand = new DeleteReceiptCommand(receiptId);
        receiptCommandService.handle(deleteReceiptCommand);
    }

    @GetMapping("{receiptId}")
    @Operation(summary = "Get Receipt by id", description = "Get Receipt by id")
    public ResponseEntity<ReceiptResource> getUser(@PathVariable Long receiptId) {
        var query = new GetReceiptByIdQuery(receiptId);
        var receiptOptional = receiptQueryService.handle(query);
        if (receiptOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var receiptResource = ReceiptResourceFromEntityAssembler.toResourceFromEntity(receiptOptional.get());
        return ResponseEntity.ok(receiptResource);
    }

}
