package me.loki2302;

import me.loki2302.client.api.TransactionOperations;
import me.loki2302.dto.ChangeLogTransactionDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.Assert.*;

public class TransactionTest extends AbstractIntegrationTest {
    private TransactionOperations transactionOperations;

    @Before
    public void createNoteOperations() {
        transactionOperations = new TransactionOperations(restTemplate);
    }

    @Test
    public void thereAreNoTransactionsByDefault() {
        List<ChangeLogTransactionDto> transactions =
                transactionOperations.getAllTransactions();
        assertTrue(transactions.isEmpty());

        try {
            transactionOperations.getFirstTransaction();
            fail();
        } catch(HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }

        try {
            transactionOperations.getLastTransaction();
            fail();
        } catch(HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }
}
