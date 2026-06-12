package pe.upc.pescagobackend.hiredService.domain.model.aggregates;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CarrierData {
    private String vehicleBrand;
    private String plate;
    private String driver;
}
