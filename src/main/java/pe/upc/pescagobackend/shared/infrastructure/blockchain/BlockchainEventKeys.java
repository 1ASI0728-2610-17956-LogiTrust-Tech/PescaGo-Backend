package pe.upc.pescagobackend.shared.infrastructure.blockchain;

import pe.upc.pescagobackend.hiredService.domain.model.aggregates.HiredService;
import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;
import pe.upc.pescagobackend.request.domain.model.aggregates.Request;
import pe.upc.pescagobackend.shared.application.LegacyStatusTranslator;
import pe.upc.pescagobackend.shared.domain.model.enums.ExecutionStatus;
import pe.upc.pescagobackend.shared.domain.model.enums.RequestStatus;

public final class BlockchainEventKeys {

    private BlockchainEventKeys() {
    }

    public static String requestCreated(Request request) {
        return "request:" + request.getId()
                + "|carrier:" + request.getCarrierId()
                + "|entrepreneur:" + request.getEntrepreneurId();
    }

    public static String requestQuoted(Request request) {
        return "request:" + request.getId()
                + "|carrier:" + request.getCarrierId()
                + "|price:" + request.getPrice();
    }

    public static String paymentRegistered(Receipt receipt) {
        return "request:" + receipt.getRequestId()
                + "|receipt:" + receipt.getId();
    }

    public static String hiredServiceCreated(HiredService hiredService) {
        return "hired:" + hiredService.getId()
                + "|request:" + hiredService.getRequestId()
                + "|carrier:" + hiredService.getCarrierId()
                + "|entrepreneur:" + hiredService.getEntrepreneurId();
    }

    public static String serviceConfirmed(HiredService hiredService) {
        return "hired:" + hiredService.getId()
                + "|request:" + hiredService.getRequestId()
                + "|carrier:" + hiredService.getCarrierId();
    }

    public static boolean isQuotedRequest(Request request) {
        return resolveRequestStatus(request.getStatus()) == RequestStatus.QUOTED;
    }

    public static boolean isConfirmedHiredService(HiredService hiredService) {
        return resolveExecutionStatus(hiredService.getStatus()) == ExecutionStatus.CONFIRMED;
    }

    private static RequestStatus resolveRequestStatus(String status) {
        try {
            return LegacyStatusTranslator.parseRequestStatusForWrite(status);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private static ExecutionStatus resolveExecutionStatus(String status) {
        try {
            return LegacyStatusTranslator.parseExecutionStatusForWrite(status);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
