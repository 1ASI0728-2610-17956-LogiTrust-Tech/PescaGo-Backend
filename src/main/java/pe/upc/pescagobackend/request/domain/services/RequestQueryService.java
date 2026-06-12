package pe.upc.pescagobackend.request.domain.services;

import pe.upc.pescagobackend.request.domain.model.aggregates.Request;
import pe.upc.pescagobackend.request.domain.model.queries.GetRequestByIdQuery;
import pe.upc.pescagobackend.request.domain.model.queries.GetRequestsByCarrierIdQuery;
import pe.upc.pescagobackend.request.domain.model.queries.GetRequestsByEntrepreneurIdQuery;

import java.util.List;
import java.util.Optional;

public interface RequestQueryService {
    Optional<Request> handle(GetRequestByIdQuery query);
    List<Request> handle (GetRequestsByCarrierIdQuery query);
    List<Request> handle (GetRequestsByEntrepreneurIdQuery query);
}
