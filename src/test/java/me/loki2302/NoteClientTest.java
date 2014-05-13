package me.loki2302;

import me.loki2302.client.LocalNote;
import me.loki2302.client.NoteClient;
import me.loki2302.client.api.NoteOperations;
import me.loki2302.client.api.TransactionOperations;
import me.loki2302.dto.NoteDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NoteClientTest extends AbstractIntegrationTest {
    private NoteOperations noteOperations;
    private TransactionOperations transactionOperations;

    @Before
    public void createNoteOperations() {
        noteOperations = new NoteOperations(restTemplate);
        transactionOperations = new TransactionOperations(restTemplate);
    }

    @Test
    @DirtiesContext
    public void canSendLocalChanges() {
        assertTrue(noteOperations.getAllNotes().isEmpty());

        NoteClient noteClient = new NoteClient(noteOperations, transactionOperations);
        noteClient.saveNote("note1", "hello", null);
        noteClient.sendChanges();

        List<NoteDto> remoteNotes = noteOperations.getAllNotes();
        assertEquals(1, remoteNotes.size());
        NoteDto remoteNote = remoteNotes.get(0);
        assertEquals("note1", remoteNote.id);
        assertEquals("hello", remoteNote.text);
    }

    @Test
    @DirtiesContext
    public void canRetrieveRemoteChanges() {
        noteOperations.putNote("note1", "hello");

        NoteClient noteClient = new NoteClient(noteOperations, transactionOperations);
        assertTrue(noteClient.getAllNotes().isEmpty());
        noteClient.retrieveChanges();

        List<LocalNote> localNotes = noteClient.getAllNotes();
        assertEquals(1, localNotes.size());
        LocalNote localNote = localNotes.get(0);
        assertEquals("note1", localNote.id);
        assertEquals("hello", localNote.text);
    }
}
