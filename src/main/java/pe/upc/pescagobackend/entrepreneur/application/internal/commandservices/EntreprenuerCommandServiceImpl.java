package pe.upc.pescagobackend.entrepreneur.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.entrepreneur.domain.model.aggregates.Entreprenuer;
import pe.upc.pescagobackend.entrepreneur.domain.model.commands.CreateEntreprenuerCommand;
import pe.upc.pescagobackend.entrepreneur.domain.model.commands.DeleteEntreprenuerCommand;
import pe.upc.pescagobackend.entrepreneur.domain.services.EntreprenuerCommandService;
import pe.upc.pescagobackend.entrepreneur.infrastructure.persistence.jpa.repositories.EntreprenuerRepository;

import java.util.Optional;

@Service
public class EntreprenuerCommandServiceImpl implements EntreprenuerCommandService {
    private final EntreprenuerRepository entreprenuerRepository;

    public EntreprenuerCommandServiceImpl(EntreprenuerRepository entreprenuerRepository) {
        this.entreprenuerRepository = entreprenuerRepository;
    }

    @Override
    public Optional<Entreprenuer> handle(CreateEntreprenuerCommand command) {
        var entreprenuer = new Entreprenuer(command);
        try {
            entreprenuerRepository.save(entreprenuer);
            return Optional.of(entreprenuer);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving the entreprenuer: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteEntreprenuerCommand command) {
       if (!entreprenuerRepository.existsById(command.entreprenuerId())) {
            throw new IllegalArgumentException("Entreprenuer with id %s not found".formatted(command.entreprenuerId()));
        }
        try {
            entreprenuerRepository.deleteById(command.entreprenuerId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting entreprenuer: %s".formatted(e.getMessage()));
        }
    }
}
