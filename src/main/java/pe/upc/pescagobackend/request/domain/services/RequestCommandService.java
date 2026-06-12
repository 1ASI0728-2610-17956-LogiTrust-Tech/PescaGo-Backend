package pe.upc.pescagobackend.request.domain.services;

import pe.upc.pescagobackend.request.domain.model.aggregates.Request;
import pe.upc.pescagobackend.request.domain.model.commands.CreateRequestCommand;
import pe.upc.pescagobackend.request.domain.model.commands.DeleteRequestCommand;
import pe.upc.pescagobackend.request.domain.model.commands.UpdateRequestCommand;

import java.util.Optional;

public interface RequestCommandService {
    void handle(CreateRequestCommand command);
    void handle(DeleteRequestCommand command);
    Optional<Request> handle(UpdateRequestCommand command);
}
