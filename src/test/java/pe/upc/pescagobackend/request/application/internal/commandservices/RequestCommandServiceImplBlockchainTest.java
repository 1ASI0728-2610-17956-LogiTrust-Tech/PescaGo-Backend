package pe.upc.pescagobackend.request.application.internal.commandservices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.upc.pescagobackend.request.domain.model.aggregates.Dimensions;
import pe.upc.pescagobackend.request.domain.model.aggregates.Request;
import pe.upc.pescagobackend.request.domain.model.commands.UpdateRequestCommand;
import pe.upc.pescagobackend.request.infrastructure.persistence.jpa.repositories.RequestRepository;
import pe.upc.pescagobackend.shared.infrastructure.blockchain.BlockchainEventType;
import pe.upc.pescagobackend.shared.infrastructure.blockchain.BlockchainTraceService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestCommandServiceImplBlockchainTest {

    @Mock
    private RequestRepository repository;

    @Mock
    private BlockchainTraceService blockchainTraceService;

    @InjectMocks
    private RequestCommandServiceImpl requestCommandService;

    @Test
    void updateRequestRecordsQuotedEventWhenStatusIsQuoted() {
        var existingRequest = requestWithId(10L, "PENDING");
        var updateCommand = updateCommand(10L, "QUOTED", 150.0);

        when(repository.findById(10L)).thenReturn(Optional.of(existingRequest));
        when(repository.save(any(Request.class))).thenAnswer(invocation -> {
            var saved = invocation.getArgument(0, Request.class);
            saved.setId(10L);
            return saved;
        });

        requestCommandService.handle(updateCommand);

        verify(blockchainTraceService).recordEvent(
                BlockchainEventType.REQUEST_QUOTED,
                "request:10|carrier:1|price:150.0",
                10L
        );
    }

    @Test
    void updateRequestDoesNotRecordQuotedEventForNonQuotedStatus() {
        var existingRequest = requestWithId(10L, "QUOTED");
        var updateCommand = updateCommand(10L, "PAID", 150.0);

        when(repository.findById(10L)).thenReturn(Optional.of(existingRequest));
        when(repository.save(any(Request.class))).thenAnswer(invocation -> invocation.getArgument(0, Request.class));

        requestCommandService.handle(updateCommand);

        verifyNoInteractions(blockchainTraceService);
    }

    private Request requestWithId(Long id, String status) {
        var request = new Request();
        request.setId(id);
        request.setEntrepreneurId(3L);
        request.setEntrepreneurName("Entrepreneur One");
        request.setCarrierId(1L);
        request.setCarrierName("Carrier One");
        request.setPackageDescription("Fish package");
        request.setQuantity(1);
        request.setWeightTotal(10.0);
        request.setPickupLocation("Lima");
        request.setDeliveryLocation("Callao");
        request.setPickupDateTime(LocalDateTime.parse("2026-06-21T10:00:00"));
        request.setPrice(0.0);
        request.setStatus(status);
        request.setDimensions(new Dimensions());
        return request;
    }

    private UpdateRequestCommand updateCommand(Long id, String status, double price) {
        return new UpdateRequestCommand(
                id,
                3L,
                "Entrepreneur One",
                1L,
                "Carrier One",
                "Fish package",
                1,
                10.0,
                "Lima",
                "Callao",
                LocalDateTime.parse("2026-06-21T10:00:00"),
                price,
                status,
                new Dimensions()
        );
    }
}
