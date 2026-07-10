package pe.upc.pescagobackend.request.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pescagobackend.hiredService.domain.model.aggregates.CarrierData;
import pe.upc.pescagobackend.hiredService.domain.model.aggregates.HiredService;
import pe.upc.pescagobackend.hiredService.domain.model.commands.CreateHiredServiceCommand;
import pe.upc.pescagobackend.hiredService.infrastructure.persistence.jpa.repositories.HiredServiceRepository;
import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;
import pe.upc.pescagobackend.receipt.domain.model.commands.CreateReceiptCommand;
import pe.upc.pescagobackend.receipt.infrastructure.persistence.jpa.repositories.ReceiptRepository;
import pe.upc.pescagobackend.request.domain.model.aggregates.Request;
import pe.upc.pescagobackend.request.infrastructure.persistence.jpa.repositories.RequestRepository;
import pe.upc.pescagobackend.request.interfaces.rest.resources.PayRequestResource;
import pe.upc.pescagobackend.request.interfaces.rest.resources.PayRequestResponse;
import pe.upc.pescagobackend.shared.application.LegacyStatusTranslator;
import pe.upc.pescagobackend.shared.domain.model.enums.ExecutionStatus;
import pe.upc.pescagobackend.shared.domain.model.enums.RequestStatus;
import pe.upc.pescagobackend.shared.infrastructure.blockchain.BlockchainEventKeys;
import pe.upc.pescagobackend.shared.infrastructure.blockchain.BlockchainEventType;
import pe.upc.pescagobackend.shared.infrastructure.blockchain.BlockchainTraceService;

import java.time.Instant;

@Service
public class RequestPaymentService {

    private final RequestRepository requestRepository;
    private final ReceiptRepository receiptRepository;
    private final HiredServiceRepository hiredServiceRepository;
    private final BlockchainTraceService blockchainTraceService;

    public RequestPaymentService(
            RequestRepository requestRepository,
            ReceiptRepository receiptRepository,
            HiredServiceRepository hiredServiceRepository,
            BlockchainTraceService blockchainTraceService
    ) {
        this.requestRepository = requestRepository;
        this.receiptRepository = receiptRepository;
        this.hiredServiceRepository = hiredServiceRepository;
        this.blockchainTraceService = blockchainTraceService;
    }

    @Transactional
    public PayRequestResponse pay(Long requestId, PayRequestResource resource) {
        if (requestId == null || requestId <= 0) {
            throw new IllegalArgumentException("Request id must be a positive number.");
        }
        if (resource == null) {
            throw new IllegalArgumentException("Payment payload cannot be null.");
        }

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));

        validatePayable(request);

        String paymentDate = resolvePaymentDate(resource.paymentDate());

        Receipt receipt = new Receipt(new CreateReceiptCommand(
                request.getId(),
                resource.holderName(),
                resource.cardNumber(),
                resource.expiryDate(),
                resource.cvv(),
                paymentDate
        ));
        Receipt savedReceipt = receiptRepository.save(receipt);
        blockchainTraceService.recordEvent(
                BlockchainEventType.PAYMENT_REGISTERED,
                BlockchainEventKeys.paymentRegistered(savedReceipt),
                savedReceipt.getRequestId()
        );

        request.setStatus(RequestStatus.PAID.name());
        Request savedRequest = requestRepository.save(request);

        CarrierData emptyCarrierData = new CarrierData();
        emptyCarrierData.setVehicleBrand("");
        emptyCarrierData.setPlate("");
        emptyCarrierData.setDriver("");

        HiredService hiredService = new HiredService(new CreateHiredServiceCommand(
                savedRequest.getId(),
                savedRequest.getEntrepreneurId(),
                savedRequest.getEntrepreneurName(),
                savedRequest.getCarrierId(),
                savedRequest.getCarrierName(),
                savedRequest.getPackageDescription(),
                savedRequest.getPickupDateTime(),
                resource.paymentMethod().trim(),
                ExecutionStatus.PENDING_CONFIRMATION.name(),
                emptyCarrierData
        ));
        HiredService savedHiredService = hiredServiceRepository.save(hiredService);
        blockchainTraceService.recordEvent(
                BlockchainEventType.HIRED_SERVICE_CREATED,
                BlockchainEventKeys.hiredServiceCreated(savedHiredService),
                savedHiredService.getId()
        );

        return new PayRequestResponse(
                savedRequest.getId(),
                savedReceipt.getId(),
                savedHiredService.getId(),
                LegacyStatusTranslator.toLegacyRequestStatus(savedRequest.getStatus()),
                resource.paymentMethod().trim()
        );
    }

    private void validatePayable(Request request) {
        RequestStatus status;
        try {
            status = LegacyStatusTranslator.parseRequestStatusForWrite(request.getStatus());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Request cannot be paid because its status is not quoted: " + request.getStatus()
            );
        }

        if (status != RequestStatus.QUOTED) {
            throw new IllegalArgumentException(
                    "Request must be quoted before payment. Current status: " + request.getStatus()
            );
        }

        if (request.getPrice() == null || request.getPrice() <= 0) {
            throw new IllegalArgumentException("Request must have a quoted price greater than zero.");
        }
    }

    private String resolvePaymentDate(String paymentDate) {
        if (paymentDate == null || paymentDate.isBlank()) {
            return Instant.now().toString();
        }
        return paymentDate.trim();
    }
}
