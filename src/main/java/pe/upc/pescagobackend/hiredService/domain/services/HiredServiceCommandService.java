package pe.upc.pescagobackend.hiredService.domain.services;

import pe.upc.pescagobackend.hiredService.domain.model.aggregates.HiredService;
import pe.upc.pescagobackend.hiredService.domain.model.commands.CreateHiredServiceCommand;
import pe.upc.pescagobackend.hiredService.domain.model.commands.DeleteHiredServiceCommand;
import pe.upc.pescagobackend.hiredService.domain.model.commands.UpdateHiredServiceCommand;

import java.util.Optional;

public interface HiredServiceCommandService {
    void handle(CreateHiredServiceCommand command);
    void handle(DeleteHiredServiceCommand command);
    Optional<HiredService> handle(UpdateHiredServiceCommand command);
}
