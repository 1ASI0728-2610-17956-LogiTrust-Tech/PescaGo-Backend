package pe.upc.pescagobackend.iam.domain.services;

import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.domain.model.queries.GetUserByAuthenticationQuery;
import pe.upc.pescagobackend.iam.domain.model.queries.GetUserByIdQuery;

import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> getUserByAuthentication(GetUserByAuthenticationQuery query);
}
