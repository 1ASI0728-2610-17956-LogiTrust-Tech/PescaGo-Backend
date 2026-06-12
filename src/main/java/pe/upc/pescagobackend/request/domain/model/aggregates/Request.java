package pe.upc.pescagobackend.request.domain.model.aggregates;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pe.upc.pescagobackend.request.domain.model.commands.CreateRequestCommand;
import pe.upc.pescagobackend.request.domain.model.commands.UpdateRequestCommand;
import pe.upc.pescagobackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


import java.time.LocalDateTime;

@Getter
@Entity
public class Request  extends AuditableAbstractAggregateRoot<Request> {

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
    private Integer quantity;
    @Setter
    private Double weightTotal;
    @Setter
    private String pickupLocation;
    @Setter
    private String deliveryLocation;
    @Setter
    private LocalDateTime pickupDateTime;
    @Setter
    private Double price;
    @Setter
    private String status;

    @Setter
    private Dimensions dimensions;

    public Request() {
        this.entrepreneurId = 0L;
        this.entrepreneurName = "";
        this.carrierId = 0L;
        this.carrierName = "";
        this.packageDescription = "";
        this.quantity = 0;
        this.weightTotal = 0.0;
        this.pickupLocation = "";
        this.deliveryLocation = "";
        this.pickupDateTime = LocalDateTime.now();
        this.price = 0.0;
        this.status = "PENDING";

        this.dimensions = new Dimensions();
    }

    public Request(CreateRequestCommand command){
        this.entrepreneurId = command.entrepreneurId();
        this.entrepreneurName = command.entrepreneurName();
        this.carrierId = command.carrierId();
        this.carrierName = command.carrierName();
        this.packageDescription = command.packageDescription();
        this.quantity = command.quantity();
        this.weightTotal = command.weightTotal();
        this.pickupLocation = command.pickupLocation();
        this.deliveryLocation = command.deliveryLocation();
        this.pickupDateTime = command.pickupDateTime();
        this.price = command.price();
        this.status = command.status();

        this.dimensions = command.dimensions();
    }

    public void UpdateRequest(UpdateRequestCommand command) {
        this.entrepreneurId = command.entrepreneurId();
        this.entrepreneurName = command.entrepreneurName();
        this.carrierId = command.carrierId();
        this.carrierName = command.carrierName();
        this.packageDescription = command.packageDescription();
        this.quantity = command.quantity();
        this.weightTotal = command.weightTotal();
        this.pickupLocation = command.pickupLocation();
        this.deliveryLocation = command.deliveryLocation();
        this.pickupDateTime = command.pickupDateTime();
        this.price = command.price();
        this.status = command.status();

        this.dimensions = command.dimensions();
    }

}