package pe.upc.pescagobackend.fleet;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import pe.upc.pescagobackend.fleet.application.internal.persistence.FleetPersistenceConstraintClassifier;
import pe.upc.pescagobackend.fleet.domain.exceptions.DuplicatePlateException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FleetPersistenceConstraintClassifierTest {

    @Test
    void recognizesUniquePlateConstraintFromHibernateConstraintViolation() {
        var sqlException = new SQLException(
                "duplicate key value violates unique constraint \"uq_fleet_vehicles_plate\"",
                "23505"
        );
        var hibernateException = new ConstraintViolationException(
                "could not execute statement",
                sqlException,
                FleetPersistenceConstraintClassifier.UNIQUE_PLATE_CONSTRAINT
        );
        var integrityViolation = new DataIntegrityViolationException("save failed", hibernateException);

        assertThat(FleetPersistenceConstraintClassifier.isUniquePlateViolation(integrityViolation)).isTrue();
        assertThatThrownBy(() -> FleetPersistenceConstraintClassifier.rethrowIfNotUniquePlate(integrityViolation, "ABC-123"))
                .isInstanceOf(DuplicatePlateException.class);
    }

    @Test
    void recognizesUniquePlateConstraintFromSqlStateAndMessage() {
        var sqlException = new SQLException(
                "ERROR: duplicate key value violates unique constraint \"uq_fleet_vehicles_plate\"",
                "23505"
        );
        var integrityViolation = new DataIntegrityViolationException("save failed", sqlException);

        assertThat(FleetPersistenceConstraintClassifier.isUniquePlateViolation(integrityViolation)).isTrue();
    }

    @Test
    void doesNotTreatForeignKeyViolationAsDuplicatePlate() {
        var sqlException = new SQLException(
                "insert or update on table \"fleet_vehicles\" violates foreign key constraint \"fk_fleet_vehicles_carrier\"",
                "23503"
        );
        var hibernateException = new ConstraintViolationException(
                "could not execute statement",
                sqlException,
                "fk_fleet_vehicles_carrier"
        );
        var integrityViolation = new DataIntegrityViolationException("save failed", hibernateException);

        assertThat(FleetPersistenceConstraintClassifier.isUniquePlateViolation(integrityViolation)).isFalse();
        assertThatThrownBy(() -> FleetPersistenceConstraintClassifier.rethrowIfNotUniquePlate(integrityViolation, "ABC-123"))
                .isInstanceOf(DataIntegrityViolationException.class)
                .isNotInstanceOf(DuplicatePlateException.class);
    }
}
