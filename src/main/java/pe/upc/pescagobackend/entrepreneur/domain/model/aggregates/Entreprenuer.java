package pe.upc.pescagobackend.entrepreneur.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pe.upc.pescagobackend.entrepreneur.domain.model.commands.CreateEntreprenuerCommand;
import pe.upc.pescagobackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Getter
@Entity
public class Entreprenuer extends AuditableAbstractAggregateRoot<Entreprenuer> {
    @Setter
    private Long userId;
    @Setter
    private String name;

    public Entreprenuer() {
        this.userId = null; // Inicialmente no está asociado a ningún usuario
        this.name = "";
    }

    public Entreprenuer(CreateEntreprenuerCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("CreateEntreprenuerCommand cannot be null.");
        }
        this.userId = command.userId();
        this.name = command.name();
    }
}
