package me.loki2302.client.api;

import me.loki2302.dto.ChangeLogTransactionDto;
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

    public List<ChangeLogTransactionDto> getAllTransactions() {
        ResponseEntity<List<ChangeLogTransactionDto>> responseEntity = restTemplate.exchange(
                buildUri("/transactions"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<ChangeLogTransactionDto>>() {});

        return responseEntity.getBody();
    }

    public List<ChangeLogTransactionDto> getTransactionsAfter(long firstTransactionId) {
        ResponseEntity<List<ChangeLogTransactionDto>> responseEntity = restTemplate.exchange(
                buildUri("/transactions/after/{id}", firstTransactionId),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<ChangeLogTransactionDto>>() {});

        return responseEntity.getBody();
    }

    public ChangeLogTransactionDto getFirstTransaction() {
        return restTemplate.getForObject(
                buildUri("/transactions/first"),
                ChangeLogTransactionDto.class);
    }

    public ChangeLogTransactionDto getLastTransaction() {
        return restTemplate.getForObject(
                buildUri("/transactions/last"),
                ChangeLogTransactionDto.class);
    }

    private static String buildUri(String path, Object... vars) {
        return UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .path(path)
                .buildAndExpand(vars)
                .toUriString();
    }
}
