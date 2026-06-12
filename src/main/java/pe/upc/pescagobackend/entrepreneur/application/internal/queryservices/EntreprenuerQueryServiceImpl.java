package pe.upc.pescagobackend.entrepreneur.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.entrepreneur.domain.model.aggregates.Entreprenuer;
import pe.upc.pescagobackend.entrepreneur.domain.model.queries.GetEntreprenuerByIdQuery;
import pe.upc.pescagobackend.entrepreneur.domain.services.EntreprenuerQueryService;
import pe.upc.pescagobackend.entrepreneur.infrastructure.persistence.jpa.repositories.EntreprenuerRepository;

import java.util.Optional;

@Service
public class EntreprenuerQueryServiceImpl implements EntreprenuerQueryService {
    private final EntreprenuerRepository entreprenuerRepository;

    public EntreprenuerQueryServiceImpl(EntreprenuerRepository entreprenuerRepository) {
        this.entreprenuerRepository = entreprenuerRepository;
    }

    @Override
    public Optional<Entreprenuer> handle(GetEntreprenuerByIdQuery query){
        return entreprenuerRepository.findByUserId(query.userId());
    }


}
