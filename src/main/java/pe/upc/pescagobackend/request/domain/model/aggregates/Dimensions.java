package pe.upc.pescagobackend.request.domain.model.aggregates;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Dimensions {
    private Double length;
    private Double width;
    private Double height;
}