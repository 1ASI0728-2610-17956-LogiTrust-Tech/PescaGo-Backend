package pe.upc.pescagobackend.shared.application;

import org.junit.jupiter.api.Test;
import pe.upc.pescagobackend.shared.domain.model.enums.ExecutionStatus;
import pe.upc.pescagobackend.shared.domain.model.enums.RequestStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LegacyStatusTranslatorTest {

    @Test
    void requestSpanishAndCanonicalInputsNormalizeToCanonicalPersistence() {
        assertThat(LegacyStatusTranslator.canonicalizeRequestStatusForPersistence("Pendiente"))
                .isEqualTo("PENDING");
        assertThat(LegacyStatusTranslator.canonicalizeRequestStatusForPersistence("PENDING"))
                .isEqualTo("PENDING");
        assertThat(LegacyStatusTranslator.canonicalizeRequestStatusForPersistence("Cotizado"))
                .isEqualTo("QUOTED");
        assertThat(LegacyStatusTranslator.canonicalizeRequestStatusForPersistence("QUOTED"))
                .isEqualTo("QUOTED");
        assertThat(LegacyStatusTranslator.canonicalizeRequestStatusForPersistence("Pagado"))
                .isEqualTo("PAID");
        assertThat(LegacyStatusTranslator.canonicalizeRequestStatusForPersistence("paid"))
                .isEqualTo("PAID");
    }

    @Test
    void executionPendingIsInterpretedAsPendingConfirmation() {
        assertThat(LegacyStatusTranslator.canonicalizeExecutionStatusForPersistence("Pendiente"))
                .isEqualTo("PENDING_CONFIRMATION");
        assertThat(LegacyStatusTranslator.canonicalizeExecutionStatusForPersistence("PENDING"))
                .isEqualTo("PENDING_CONFIRMATION");
        assertThat(LegacyStatusTranslator.canonicalizeExecutionStatusForPersistence("PENDING_CONFIRMATION"))
                .isEqualTo("PENDING_CONFIRMATION");
    }

    @Test
    void legacyV1OutputUsesSpanishByContext() {
        assertThat(LegacyStatusTranslator.toLegacyRequestStatus("PENDING")).isEqualTo("Pendiente");
        assertThat(LegacyStatusTranslator.toLegacyRequestStatus("QUOTED")).isEqualTo("Cotizado");
        assertThat(LegacyStatusTranslator.toLegacyRequestStatus("PAID")).isEqualTo("Pagado");

        assertThat(LegacyStatusTranslator.toLegacyExecutionStatus("PENDING_CONFIRMATION")).isEqualTo("Pendiente");
        assertThat(LegacyStatusTranslator.toLegacyExecutionStatus("PENDING")).isEqualTo("Pendiente");
        assertThat(LegacyStatusTranslator.toLegacyExecutionStatus("CONFIRMED")).isEqualTo("Confirmado");
    }

    @Test
    void unknownStoredValuesAreReturnedUnchangedOnRead() {
        assertThat(LegacyStatusTranslator.toLegacyRequestStatus("Aceptado")).isEqualTo("Aceptado");
        assertThat(LegacyStatusTranslator.toLegacyExecutionStatus("EnTransito")).isEqualTo("EnTransito");
    }

    @Test
    void invalidWriteValuesThrowValidationException() {
        assertThatThrownBy(() -> LegacyStatusTranslator.parseRequestStatusForWrite("Confirmado"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported request status for write");

        assertThatThrownBy(() -> LegacyStatusTranslator.parseExecutionStatusForWrite("Cotizado"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported execution status for write");

        assertThatThrownBy(() -> LegacyStatusTranslator.parseRequestStatusForWrite("Aceptado"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(LegacyStatusTranslator.parseRequestStatusForWrite("Pendiente"))
                .isEqualTo(RequestStatus.PENDING);
        assertThat(LegacyStatusTranslator.parseExecutionStatusForWrite("Confirmado"))
                .isEqualTo(ExecutionStatus.CONFIRMED);
    }
}
