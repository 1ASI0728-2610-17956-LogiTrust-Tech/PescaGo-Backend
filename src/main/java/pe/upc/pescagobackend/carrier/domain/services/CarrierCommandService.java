package pe.upc.pescagobackend.carrier.domain.services;

import pe.upc.pescagobackend.carrier.domain.model.aggregates.Carrier;
import pe.upc.pescagobackend.carrier.domain.model.commands.CreateCarrierCommand;
import pe.upc.pescagobackend.carrier.domain.model.commands.DeleteCarrierCommand;

import java.util.Optional;

public interface CarrierCommandService {
    Optional<Carrier> handle(CreateCarrierCommand command);
    void handle(DeleteCarrierCommand command);
}
