package pe.upc.pescagobackend.carrier.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.carrier.domain.model.aggregates.Carrier;
import pe.upc.pescagobackend.carrier.domain.model.commands.CreateCarrierCommand;
import pe.upc.pescagobackend.carrier.domain.model.commands.DeleteCarrierCommand;
import pe.upc.pescagobackend.carrier.domain.services.CarrierCommandService;
import pe.upc.pescagobackend.carrier.infrastructure.persistence.jpa.repositories.CarrierRepository;

import java.util.Optional;

@Service
public class CarrierCommandServiceImpl implements CarrierCommandService {
    private final CarrierRepository carrierRepository;

    public CarrierCommandServiceImpl(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    @Override
    public Optional<Carrier> handle(CreateCarrierCommand command) {
        var carrier = new Carrier(command);
        try {
            carrierRepository.save(carrier);
            return Optional.of(carrier);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving the carrier: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteCarrierCommand command) {
       if (!carrierRepository.existsById(command.carrierId())) {
            throw new IllegalArgumentException("Carrier with id %s not found".formatted(command.carrierId()));
        }
        try {
            carrierRepository.deleteById(command.carrierId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting carrier: %s".formatted(e.getMessage()));
        }
    }
}
