package pe.upc.pescagobackend.hiredService.domain.services;

import pe.upc.pescagobackend.hiredService.domain.model.aggregates.HiredService;
import pe.upc.pescagobackend.hiredService.domain.model.queries.GetHiredServiceByIdQuery;
import pe.upc.pescagobackend.hiredService.domain.model.queries.GetHiredServicesByCarrierIdQuery;
import pe.upc.pescagobackend.hiredService.domain.model.queries.GetHiredServicesByEntrepreneurIdQuery;

import java.util.List;
import java.util.Optional;

public interface HiredServiceQueryService {
    Optional<HiredService> handle(GetHiredServiceByIdQuery query);
    List<HiredService> handle(GetHiredServicesByEntrepreneurIdQuery query);
    List<HiredService> handle(GetHiredServicesByCarrierIdQuery query);
}
