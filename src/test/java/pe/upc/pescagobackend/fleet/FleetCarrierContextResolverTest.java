package pe.upc.pescagobackend.fleet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.upc.pescagobackend.carrier.domain.model.aggregates.Carrier;
import pe.upc.pescagobackend.carrier.infrastructure.persistence.jpa.repositories.CarrierRepository;
import pe.upc.pescagobackend.fleet.application.internal.services.FleetCarrierContextResolver;
import pe.upc.pescagobackend.shared.domain.model.enums.Role;
import pe.upc.pescagobackend.shared.infrastructure.security.AuthenticatedUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FleetCarrierContextResolverTest {

    @Mock
    private CarrierRepository carrierRepository;

    @InjectMocks
    private FleetCarrierContextResolver fleetCarrierContextResolver;

    @Test
    void resolveCarrierIdReturnsCarrierLinkedToAuthenticatedUser() {
        var user = new AuthenticatedUser(10L, "carrier@example.com", Role.CARRIER, "hash");
        var carrier = new Carrier();
        carrier.setUserId(10L);
        carrier.setId(42L);

        when(carrierRepository.findByUserId(10L)).thenReturn(Optional.of(carrier));

        assertThat(fleetCarrierContextResolver.resolveCarrierId(user)).contains(42L);
    }

    @Test
    void resolveCarrierIdReturnsEmptyWhenCarrierProfileMissing() {
        var user = new AuthenticatedUser(11L, "orphan@example.com", Role.CARRIER, "hash");
        when(carrierRepository.findByUserId(11L)).thenReturn(Optional.empty());

        assertThat(fleetCarrierContextResolver.resolveCarrierId(user)).isEmpty();
    }
}
