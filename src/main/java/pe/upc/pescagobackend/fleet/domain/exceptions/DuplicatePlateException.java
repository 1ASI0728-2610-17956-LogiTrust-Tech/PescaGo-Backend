package pe.upc.pescagobackend.fleet.domain.exceptions;

public class DuplicatePlateException extends RuntimeException {

    public DuplicatePlateException(String plate) {
        super("Vehicle plate already exists: %s".formatted(plate));
    }
}
