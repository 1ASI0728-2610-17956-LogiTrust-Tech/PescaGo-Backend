package pe.upc.pescagobackend.carrier.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pe.upc.pescagobackend.carrier.domain.model.commands.CreateCarrierCommand;
import pe.upc.pescagobackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Getter
@Entity
public class Carrier extends AuditableAbstractAggregateRoot<Carrier> {
    @Setter
    private Long userId;
    @Setter
    private String name;
    @Setter
    private String description;

    public Carrier() {
        this.userId = null; // Inicialmente no está asociado a ningún usuario
        this.name = "";
        this.description = "";
    }

    public Carrier(CreateCarrierCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("CreateCarrierCommand cannot be null.");
        }
        this.userId = command.userId();
        this.name = command.name();
        this.description = command.description();
    }
}
