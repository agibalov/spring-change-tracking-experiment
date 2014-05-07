package me.loki2302;

import me.loki2302.entities.ChangeLogTransaction;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class TransactionOperations {
    private final RestTemplate restTemplate;

    public TransactionOperations(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ChangeLogTransaction> getAllTransactions() {
        ResponseEntity<List<ChangeLogTransaction>> responseEntity = restTemplate.exchange(
                buildUri("/transactions"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<ChangeLogTransaction>>() {});

        return responseEntity.getBody();
    }

    public List<ChangeLogTransaction> getTransactionsAfter(String firstTransactionId) {
        ResponseEntity<List<ChangeLogTransaction>> responseEntity = restTemplate.exchange(
                buildUri("/transactions/after/{id}", firstTransactionId),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<ChangeLogTransaction>>() {});

        return responseEntity.getBody();
    }

    public ChangeLogTransaction getFirstTransaction() {
        return restTemplate.getForObject(buildUri("/transactions/first"), ChangeLogTransaction.class);
    }

    public ChangeLogTransaction getLastTransaction() {
        return restTemplate.getForObject(buildUri("/transactions/last"), ChangeLogTransaction.class);
    }

    private static String buildUri(String path, Object... vars) {
        return UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .path(path)
                .buildAndExpand(vars)
                .toUriString();
    }
}
