package pe.upc.pescagobackend.hiredService.application.internal.commandservices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.upc.pescagobackend.hiredService.domain.model.aggregates.CarrierData;
import pe.upc.pescagobackend.hiredService.domain.model.aggregates.HiredService;
import pe.upc.pescagobackend.hiredService.domain.model.commands.CreateHiredServiceCommand;
import pe.upc.pescagobackend.hiredService.domain.model.commands.UpdateHiredServiceCommand;
import pe.upc.pescagobackend.hiredService.infrastructure.persistence.jpa.repositories.HiredServiceRepository;
import pe.upc.pescagobackend.shared.infrastructure.blockchain.BlockchainEventType;
import pe.upc.pescagobackend.shared.infrastructure.blockchain.BlockchainTraceService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HIredServiceCommandServiceImplBlockchainTest {

    @Mock
    private HiredServiceRepository hiredServiceRepository;

    @Mock
    private BlockchainTraceService blockchainTraceService;

    @InjectMocks
    private HIredServiceCommandServiceImpl hiredServiceCommandService;

    @Test
    void createHiredServiceRecordsHiredServiceCreatedEvent() {
        var command = createCommand("PENDING_CONFIRMATION");

        when(hiredServiceRepository.save(any(HiredService.class))).thenAnswer(invocation -> {
            var saved = invocation.getArgument(0, HiredService.class);
            saved.setId(12L);
            return saved;
        });

        hiredServiceCommandService.handle(command);

        verify(blockchainTraceService).recordEvent(
                BlockchainEventType.HIRED_SERVICE_CREATED,
                "hired:12|request:42|carrier:1|entrepreneur:3",
                12L
        );
    }

    @Test
    void updateHiredServiceRecordsServiceConfirmedWhenStatusIsConfirmed() {
        var existingService = hiredServiceWithId(12L, "PENDING_CONFIRMATION");
        var updateCommand = updateCommand(12L, "CONFIRMED");

        when(hiredServiceRepository.findById(12L)).thenReturn(Optional.of(existingService));
        when(hiredServiceRepository.save(any(HiredService.class))).thenAnswer(invocation -> {
            var saved = invocation.getArgument(0, HiredService.class);
            saved.setId(12L);
            return saved;
        });

        hiredServiceCommandService.handle(updateCommand);

        verify(blockchainTraceService).recordEvent(
                BlockchainEventType.SERVICE_CONFIRMED,
                "hired:12|request:42|carrier:1",
                12L
        );
    }

    @Test
    void updateHiredServiceDoesNotRecordServiceConfirmedForPendingStatus() {
        var existingService = hiredServiceWithId(12L, "PENDING_CONFIRMATION");
        var updateCommand = updateCommand(12L, "PENDING_CONFIRMATION");

        when(hiredServiceRepository.findById(12L)).thenReturn(Optional.of(existingService));
        when(hiredServiceRepository.save(any(HiredService.class))).thenAnswer(invocation -> invocation.getArgument(0, HiredService.class));

        hiredServiceCommandService.handle(updateCommand);

        verifyNoInteractions(blockchainTraceService);
    }

    private CreateHiredServiceCommand createCommand(String status) {
        return new CreateHiredServiceCommand(
                42L,
                3L,
                "Entrepreneur One",
                1L,
                "Carrier One",
                "Fish package",
                LocalDateTime.parse("2026-06-21T10:00:00"),
                "CARD",
                status,
                new CarrierData()
        );
    }

    private UpdateHiredServiceCommand updateCommand(Long id, String status) {
        return new UpdateHiredServiceCommand(
                id,
                42L,
                3L,
                "Entrepreneur One",
                1L,
                "Carrier One",
                "Fish package",
                LocalDateTime.parse("2026-06-21T10:00:00"),
                "CARD",
                status,
                new CarrierData()
        );
    }

    private HiredService hiredServiceWithId(Long id, String status) {
        var hiredService = new HiredService();
        hiredService.setId(id);
        hiredService.setRequestId(42L);
        hiredService.setEntrepreneurId(3L);
        hiredService.setEntrepreneurName("Entrepreneur One");
        hiredService.setCarrierId(1L);
        hiredService.setCarrierName("Carrier One");
        hiredService.setPackageDescription("Fish package");
        hiredService.setPickupDateTime(LocalDateTime.parse("2026-06-21T10:00:00"));
        hiredService.setStatus(status);
        hiredService.setPaymentMethod("CARD");
        hiredService.setCarrierData(new CarrierData());
        return hiredService;
    }
}
