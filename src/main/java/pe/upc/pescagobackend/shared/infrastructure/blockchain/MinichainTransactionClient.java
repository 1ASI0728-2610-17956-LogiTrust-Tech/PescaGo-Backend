package pe.upc.pescagobackend.shared.infrastructure.blockchain;

import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Component
public class MinichainTransactionClient {

    private final RestClient restClient;

    public MinichainTransactionClient(BlockchainProperties properties) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(properties.connectTimeoutMs()));
        requestFactory.setReadTimeout(Duration.ofMillis(properties.readTimeoutMs()));

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

    public void createTransaction(String baseUrl, String from, String to, long amount) {
        var normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;

        restClient.post()
                .uri(normalizedBaseUrl + "/transactions/new")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TransactionPayload(from, to, amount))
                .retrieve()
                .toBodilessEntity();
    }

    private record TransactionPayload(String from, String to, long amount) {
    }
}
