package pe.upc.pescagobackend.entrepreneur.domain.services;

import pe.upc.pescagobackend.entrepreneur.domain.model.aggregates.Entreprenuer;
import pe.upc.pescagobackend.entrepreneur.domain.model.queries.GetEntreprenuerByIdQuery;

import java.util.Optional;

public interface EntreprenuerQueryService {
    Optional<Entreprenuer> handle(GetEntreprenuerByIdQuery query);
}
