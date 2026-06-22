package pe.upc.pescagobackend.iam.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.carrier.domain.model.aggregates.Carrier;
import pe.upc.pescagobackend.carrier.domain.model.queries.GetCarrierByUserIdQuery;
import pe.upc.pescagobackend.carrier.domain.services.CarrierQueryService;
import pe.upc.pescagobackend.carrier.interfaces.rest.transform.CarrierResourceFromEntityAssembler;
import pe.upc.pescagobackend.entrepreneur.domain.model.queries.GetEntreprenuerByIdQuery;
import pe.upc.pescagobackend.entrepreneur.domain.services.EntreprenuerQueryService;
import pe.upc.pescagobackend.entrepreneur.domain.model.aggregates.Entreprenuer;
import pe.upc.pescagobackend.entrepreneur.interfaces.rest.transform.EntreprenuerResourceFromEntityAssembler;
import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.BusinessProfileResource;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.SessionProfileResource;
import pe.upc.pescagobackend.shared.application.RoleCompatibilityMapper;
import pe.upc.pescagobackend.shared.domain.model.enums.Role;

import java.util.Optional;

@Service
public class UserProfileQueryService {

    private final UserRepository userRepository;
    private final CarrierQueryService carrierQueryService;
    private final EntreprenuerQueryService entreprenuerQueryService;

    public UserProfileQueryService(
            UserRepository userRepository,
            CarrierQueryService carrierQueryService,
            EntreprenuerQueryService entreprenuerQueryService
    ) {
        this.userRepository = userRepository;
        this.carrierQueryService = carrierQueryService;
        this.entreprenuerQueryService = entreprenuerQueryService;
    }

    public Optional<SessionProfileResource> getSessionProfile(Long userId) {
        return userRepository.findById(userId).map(this::toSessionProfile);
    }

    public SessionProfileResource toSessionProfile(User user) {
        Role canonicalRole = RoleCompatibilityMapper.toCanonicalRole(user.getRole());
        BusinessProfileResource businessProfile = resolveBusinessProfile(user.getId(), canonicalRole);
        return new SessionProfileResource(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                canonicalRole.name(),
                businessProfile
        );
    }

    private BusinessProfileResource resolveBusinessProfile(Long userId, Role canonicalRole) {
        return switch (canonicalRole) {
            case CARRIER -> carrierQueryService.handle(new GetCarrierByUserIdQuery(userId))
                    .map(this::toCarrierBusinessProfile)
                    .orElse(null);
            case ENTREPRENEUR -> entreprenuerQueryService.handle(new GetEntreprenuerByIdQuery(userId))
                    .map(this::toEntrepreneurBusinessProfile)
                    .orElse(null);
            default -> null;
        };
    }

    private BusinessProfileResource toCarrierBusinessProfile(Carrier carrier) {
        var resource = CarrierResourceFromEntityAssembler.toResourceFromEntity(carrier);
        return new BusinessProfileResource(
                "carrier",
                resource.id(),
                resource.userId(),
                resource.name(),
                resource.description()
        );
    }

    private BusinessProfileResource toEntrepreneurBusinessProfile(Entreprenuer entrepreneur) {
        var resource = EntreprenuerResourceFromEntityAssembler.toResourceFromEntity(entrepreneur);
        return new BusinessProfileResource(
                "entrepreneur",
                resource.id(),
                resource.userId(),
                resource.name(),
                null
        );
    }
}
