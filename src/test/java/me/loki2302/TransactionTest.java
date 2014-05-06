package me.loki2302;

import me.loki2302.dto.NoteDto;
import me.loki2302.dto.NoteFieldsDto;
import me.loki2302.entities.ChangeLogEvent;
import me.loki2302.entities.ChangeLogTransaction;
import me.loki2302.entities.CreateEntityChangeLogEvent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.Assert.*;

public class TransactionTest extends AbstractIntegrationTest {
    private static String buildUri(String path, Object... vars) {
        return UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .path(path)
                .buildAndExpand(vars)
                .toUriString();
    }

    @Test
    public void thereAreNoTransactionsByDefault() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<ChangeLogTransaction>> responseEntity = restTemplate.exchange(
                buildUri("/transactions"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<ChangeLogTransaction>>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isEmpty());

        try {
            restTemplate.getForObject(buildUri("/transactions/first"), ChangeLogTransaction.class);
            fail();
        } catch(HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }

        try {
            restTemplate.getForObject(buildUri("/transactions/last"), ChangeLogTransaction.class);
            fail();
        } catch(HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }
}
