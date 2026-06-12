package pe.upc.pescagobackend.request.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.request.domain.model.aggregates.Request;
import pe.upc.pescagobackend.request.domain.model.commands.CreateRequestCommand;
import pe.upc.pescagobackend.request.domain.model.commands.DeleteRequestCommand;
import pe.upc.pescagobackend.request.domain.model.commands.UpdateRequestCommand;
import pe.upc.pescagobackend.request.domain.services.RequestCommandService;
import pe.upc.pescagobackend.request.infrastructure.persistence.jpa.repositories.RequestRepository;

import java.util.Optional;

@Service
public class RequestCommandServiceImpl implements RequestCommandService {

    private final RequestRepository repository;

    public RequestCommandServiceImpl(RequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(CreateRequestCommand command) {
        var request = new Request(command);
        try {
            repository.save(request);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving the request: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteRequestCommand command) {
        if (!repository.existsById(command.id())) {
            throw new IllegalArgumentException("Request with id %s not found".formatted(command.id()));
        }
        try {
            repository.deleteById(command.id());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting request: %s".formatted(e.getMessage()));
        }

    }

    @Override
    public Optional<Request> handle(UpdateRequestCommand command) {
        var requestOptional = repository.findById(command.id());
        if (requestOptional.isEmpty()) {
            return Optional.empty();
        }
        var request = requestOptional.get();
        request.UpdateRequest(command);
        try {
            repository.save(request);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating the request: %s".formatted(e.getMessage()));
        }
        return Optional.of(request);
    }
}
