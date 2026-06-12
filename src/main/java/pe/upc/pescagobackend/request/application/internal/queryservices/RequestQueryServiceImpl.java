package pe.upc.pescagobackend.request.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.request.domain.model.aggregates.Request;
import pe.upc.pescagobackend.request.domain.model.queries.GetRequestByIdQuery;
import pe.upc.pescagobackend.request.domain.model.queries.GetRequestsByCarrierIdQuery;
import pe.upc.pescagobackend.request.domain.model.queries.GetRequestsByEntrepreneurIdQuery;
import pe.upc.pescagobackend.request.domain.services.RequestQueryService;
import pe.upc.pescagobackend.request.infrastructure.persistence.jpa.repositories.RequestRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RequestQueryServiceImpl implements RequestQueryService {
    private final RequestRepository requestRepository;

    public RequestQueryServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Optional<Request> handle(GetRequestByIdQuery query) {
        return requestRepository.findById(query.id());
    }

    @Override
    public List<Request> handle(GetRequestsByCarrierIdQuery query) {
        return requestRepository.findByCarrierId(query.carrierId());
    }

    @Override
    public List<Request> handle(GetRequestsByEntrepreneurIdQuery query) {
        return requestRepository.findByEntrepreneurId(query.entrepreneurId());
    }

}
