package pe.upc.pescagobackend.fleet.application.internal.persistence;

import org.springframework.dao.DataIntegrityViolationException;
import pe.upc.pescagobackend.fleet.domain.exceptions.DuplicatePlateException;

import java.sql.SQLException;

public final class FleetPersistenceConstraintClassifier {

    public static final String UNIQUE_PLATE_CONSTRAINT = "uq_fleet_vehicles_plate";
    private static final String UNIQUE_VIOLATION_SQL_STATE = "23505";

    private FleetPersistenceConstraintClassifier() {
    }

    public static boolean isUniquePlateViolation(DataIntegrityViolationException exception) {
        Throwable current = exception;
        while (current != null) {
            if (current instanceof org.hibernate.exception.ConstraintViolationException constraintViolation) {
                if (UNIQUE_PLATE_CONSTRAINT.equals(constraintViolation.getConstraintName())) {
                    return true;
                }
            }
            if (current instanceof SQLException sqlException) {
                if (UNIQUE_VIOLATION_SQL_STATE.equals(sqlException.getSQLState())
                        && mentionsUniquePlateConstraint(sqlException.getMessage())) {
                    return true;
                }
            }
            if (mentionsUniquePlateConstraint(current.getMessage())) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    public static void rethrowIfNotUniquePlate(DataIntegrityViolationException exception, String plate) {
        if (isUniquePlateViolation(exception)) {
            throw new DuplicatePlateException(plate);
        }
        throw exception;
    }

    private static boolean mentionsUniquePlateConstraint(String message) {
        return message != null && message.contains(UNIQUE_PLATE_CONSTRAINT);
    }
}
