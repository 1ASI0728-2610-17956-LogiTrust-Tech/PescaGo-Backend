package pe.upc.pescagobackend.request.interfaces.rest.resources;

public record PayRequestResponse(
        Long requestId,
        Long receiptId,
        Long hiredServiceId,
        String status,
        String paymentMethod
) {
}
