package pe.upc.pescagobackend.hiredService.domain.model.aggregates;

import pe.upc.pescagobackend.hiredService.domain.model.commands.CreateHiredServiceCommand;
import pe.upc.pescagobackend.hiredService.domain.model.commands.UpdateHiredServiceCommand;
import pe.upc.pescagobackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
public class HiredService extends AuditableAbstractAggregateRoot<HiredService> {

    @Setter
    private Long requestId;
    @Setter
    private Long entrepreneurId;
    @Setter
    private String entrepreneurName;
    @Setter
    private Long carrierId;
    @Setter
    private String carrierName;
    @Setter
    private String packageDescription;
    @Setter
    private LocalDateTime pickupDateTime;

    @Setter
    private String status;
    @Setter
    private String paymentMethod;

    @Setter
    private CarrierData carrierData;

    public HiredService() {
        this.requestId = 0L;
        this.entrepreneurId = 0L;
        this.entrepreneurName = "";
        this.carrierId = 0L;
        this.carrierName = "";
        this.packageDescription = "";
        this.pickupDateTime = LocalDateTime.now();
        this.status = "PENDING";
        this.paymentMethod = "";

        this.carrierData = new CarrierData();
    }

    public HiredService(CreateHiredServiceCommand command) {
        this.requestId = command.requestId();
        this.entrepreneurId = command.entrepreneurId();
        this.entrepreneurName = command.entrepreneurName();
        this.carrierId = command.carrierId();
        this.carrierName = command.carrierName();
        this.packageDescription = command.packageDescription();
        this.pickupDateTime = command.pickupDateTime();
        this.status = command.status();
        this.paymentMethod = command.paymentMethod();

        this.carrierData = command.carrierData();
    }

    public HiredService(UpdateHiredServiceCommand command) {
        this.requestId = command.requestId();
        this.entrepreneurId = command.entrepreneurId();
        this.entrepreneurName = command.entrepreneurName();
        this.carrierId = command.carrierId();
        this.carrierName = command.carrierName();
        this.packageDescription = command.packageDescription();
        this.pickupDateTime = command.pickupDateTime();
        this.status = command.status();
        this.paymentMethod = command.paymentMethod();

        this.carrierData = command.carrierData();
    }

    public void UpdateHiredService(UpdateHiredServiceCommand command) {
        this.requestId = command.requestId();
        this.entrepreneurId = command.entrepreneurId();
        this.entrepreneurName = command.entrepreneurName();
        this.carrierId = command.carrierId();
        this.carrierName = command.carrierName();
        this.packageDescription = command.packageDescription();
        this.pickupDateTime = command.pickupDateTime();
        this.status = command.status();
        this.paymentMethod = command.paymentMethod();

        this.carrierData = command.carrierData();
    }
}