package pe.upc.pescagobackend.shared.application;

import pe.upc.pescagobackend.shared.domain.model.enums.ExecutionStatus;
import pe.upc.pescagobackend.shared.domain.model.enums.RequestStatus;

import java.util.Locale;
import java.util.Optional;

public final class LegacyStatusTranslator {

    private LegacyStatusTranslator() {
    }

    public static String canonicalizeRequestStatusForPersistence(String rawStatus) {
        return parseRequestStatusForWrite(rawStatus).name();
    }

    public static String canonicalizeExecutionStatusForPersistence(String rawStatus) {
        return parseExecutionStatusForWrite(rawStatus).name();
    }

    public static String toLegacyRequestStatus(String storedStatus) {
        if (storedStatus == null || storedStatus.isBlank()) {
            return storedStatus;
        }

        return resolveRequestStatus(storedStatus)
                .map(LegacyStatusTranslator::toLegacyRequestLabel)
                .orElse(storedStatus);
    }

    public static String toLegacyExecutionStatus(String storedStatus) {
        if (storedStatus == null || storedStatus.isBlank()) {
            return storedStatus;
        }

        return resolveExecutionStatus(storedStatus)
                .map(LegacyStatusTranslator::toLegacyExecutionLabel)
                .orElse(storedStatus);
    }

    public static RequestStatus parseRequestStatusForWrite(String rawStatus) {
        if (rawStatus == null || rawStatus.isBlank()) {
            throw new IllegalArgumentException("Request status cannot be null or blank");
        }

        return resolveRequestStatus(rawStatus)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported request status for write: " + rawStatus
                ));
    }

    public static ExecutionStatus parseExecutionStatusForWrite(String rawStatus) {
        if (rawStatus == null || rawStatus.isBlank()) {
            throw new IllegalArgumentException("Execution status cannot be null or blank");
        }

        return resolveExecutionStatus(rawStatus)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported execution status for write: " + rawStatus
                ));
    }

    private static Optional<RequestStatus> resolveRequestStatus(String rawStatus) {
        return switch (normalize(rawStatus)) {
            case "pendiente", "pending" -> Optional.of(RequestStatus.PENDING);
            case "cotizado", "quoted" -> Optional.of(RequestStatus.QUOTED);
            case "pagado", "paid" -> Optional.of(RequestStatus.PAID);
            default -> Optional.empty();
        };
    }

    private static Optional<ExecutionStatus> resolveExecutionStatus(String rawStatus) {
        return switch (normalize(rawStatus)) {
            case "pendiente", "pending", "pending_confirmation" -> Optional.of(ExecutionStatus.PENDING_CONFIRMATION);
            case "confirmado", "confirmed" -> Optional.of(ExecutionStatus.CONFIRMED);
            default -> Optional.empty();
        };
    }

    private static String toLegacyRequestLabel(RequestStatus status) {
        return switch (status) {
            case PENDING -> "Pendiente";
            case QUOTED -> "Cotizado";
            case PAID -> "Pagado";
        };
    }

    private static String toLegacyExecutionLabel(ExecutionStatus status) {
        return switch (status) {
            case PENDING_CONFIRMATION -> "Pendiente";
            case CONFIRMED -> "Confirmado";
        };
    }

    private static String normalize(String rawStatus) {
        return rawStatus.trim().toLowerCase(Locale.ROOT);
    }
}
