package pe.upc.pescagobackend.iam.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.iam.application.internal.authenticationservices.PasswordAuthenticationService;
import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.domain.model.queries.GetUserByAuthenticationQuery;
import pe.upc.pescagobackend.iam.domain.model.queries.GetUserByIdQuery;
import pe.upc.pescagobackend.iam.domain.services.UserQueryService;
import pe.upc.pescagobackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private final PasswordAuthenticationService passwordAuthenticationService;

    public UserQueryServiceImpl(
            UserRepository userRepository,
            PasswordAuthenticationService passwordAuthenticationService
    ) {
        this.userRepository = userRepository;
        this.passwordAuthenticationService = passwordAuthenticationService;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query){
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> getUserByAuthentication(GetUserByAuthenticationQuery query) {
        if (query == null || query.email() == null || query.password() == null) {
            throw new IllegalArgumentException("Email and password must not be null.");
        }
        return passwordAuthenticationService.authenticate(query.email(), query.password());
    }


}
