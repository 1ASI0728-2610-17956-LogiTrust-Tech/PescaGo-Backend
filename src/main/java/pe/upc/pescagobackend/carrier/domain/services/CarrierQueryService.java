package pe.upc.pescagobackend.carrier.domain.services;

import pe.upc.pescagobackend.carrier.domain.model.aggregates.Carrier;
import pe.upc.pescagobackend.carrier.domain.model.queries.GetCarrierByIdQuery;
import pe.upc.pescagobackend.carrier.domain.model.queries.GetCarriersQuery;

import java.util.List;
import java.util.Optional;

public interface CarrierQueryService {
    Optional<Carrier> handle(GetCarrierByIdQuery query);
    Optional<List<Carrier>> handle(GetCarriersQuery query);
}
