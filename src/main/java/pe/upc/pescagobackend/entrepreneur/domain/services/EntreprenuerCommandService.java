package pe.upc.pescagobackend.entrepreneur.domain.services;

import pe.upc.pescagobackend.entrepreneur.domain.model.aggregates.Entreprenuer;
import pe.upc.pescagobackend.entrepreneur.domain.model.commands.CreateEntreprenuerCommand;
import pe.upc.pescagobackend.entrepreneur.domain.model.commands.DeleteEntreprenuerCommand;

import java.util.Optional;

public interface EntreprenuerCommandService {
    Optional<Entreprenuer> handle(CreateEntreprenuerCommand command);
    void handle(DeleteEntreprenuerCommand command);
}
