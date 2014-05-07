package me.loki2302;

import me.loki2302.entities.ChangeLogTransaction;
import me.loki2302.entities.CreateEntityChangeLogEvent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompositeTest extends AbstractIntegrationTest {
    private NoteOperations noteOperations;
    private TransactionOperations transactionOperations;

    @Before
    public void createNoteOperations() {
        noteOperations = new NoteOperations(restTemplate);
        transactionOperations = new TransactionOperations(restTemplate);
    }

    @Test
    @DirtiesContext
    public void dummy() {
        assertTrue(transactionOperations.getAllTransactions().isEmpty());

        noteOperations.createNote("note1", "hello");

        List<ChangeLogTransaction> transactions = transactionOperations.getAllTransactions();
        assertEquals(1, transactions.size());
        assertEquals(1, transactions.get(0).events.size());
        assertTrue(transactions.get(0).events.get(0) instanceof CreateEntityChangeLogEvent);

        // TODO: create note #2
        // TODO: update note #1
        // TODO: delete note #2
    }
}
