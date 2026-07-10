package pe.upc.pescagobackend.shared.infrastructure.blockchain;

import org.junit.jupiter.api.Test;
import pe.upc.pescagobackend.hiredService.domain.model.aggregates.CarrierData;
import pe.upc.pescagobackend.hiredService.domain.model.aggregates.HiredService;
import pe.upc.pescagobackend.receipt.domain.model.aggregates.Receipt;
import pe.upc.pescagobackend.request.domain.model.aggregates.Request;

import static org.assertj.core.api.Assertions.assertThat;

class BlockchainEventKeysTest {

    @Test
    void detectsQuotedRequestFromCanonicalAndLegacyStatus() {
        var quoted = new Request();
        quoted.setStatus("QUOTED");

        var legacyQuoted = new Request();
        legacyQuoted.setStatus("Cotizado");

        assertThat(BlockchainEventKeys.isQuotedRequest(quoted)).isTrue();
        assertThat(BlockchainEventKeys.isQuotedRequest(legacyQuoted)).isTrue();
    }

    @Test
    void detectsConfirmedHiredServiceFromCanonicalAndLegacyStatus() {
        var confirmed = new HiredService();
        confirmed.setStatus("CONFIRMED");

        var legacyConfirmed = new HiredService();
        legacyConfirmed.setStatus("Confirmado");

        assertThat(BlockchainEventKeys.isConfirmedHiredService(confirmed)).isTrue();
        assertThat(BlockchainEventKeys.isConfirmedHiredService(legacyConfirmed)).isTrue();
    }

    @Test
    void buildsPaymentRegisteredKeyWithoutSensitiveFields() {
        var receipt = new Receipt();
        receipt.setId(15L);
        receipt.setRequestId(42L);
        receipt.setCardNumber("4111111111111111");
        receipt.setCvv("123");

        assertThat(BlockchainEventKeys.paymentRegistered(receipt))
                .isEqualTo("request:42|receipt:15");
    }
}
