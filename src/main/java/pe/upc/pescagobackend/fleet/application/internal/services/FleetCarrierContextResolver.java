package pe.upc.pescagobackend.fleet.application.internal.services;

import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.carrier.infrastructure.persistence.jpa.repositories.CarrierRepository;
import pe.upc.pescagobackend.shared.infrastructure.security.AuthenticatedUser;

import java.util.Optional;

@Service
public class FleetCarrierContextResolver {

    private final CarrierRepository carrierRepository;

    public FleetCarrierContextResolver(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    public Optional<Long> resolveCarrierId(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null || authenticatedUser.getUserId() == null) {
            return Optional.empty();
        }
        return carrierRepository.findByUserId(authenticatedUser.getUserId())
                .map(carrier -> carrier.getId());
    }
}
