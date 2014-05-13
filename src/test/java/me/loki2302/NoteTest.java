package me.loki2302;

import me.loki2302.client.api.NoteOperations;
import me.loki2302.dto.NoteDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NoteTest extends AbstractIntegrationTest {
    private NoteOperations noteOperations;

    @Before
    public void createNoteOperations() {
        noteOperations = new NoteOperations(restTemplate);
    }

    @Test
    public void thereAreNoNotesByDefault() {
        List<NoteDto> noteDtos = noteOperations.getAllNotes();
        assertTrue(noteDtos.isEmpty());
    }

    @Test
    @DirtiesContext
    public void canCreateNote() {
        NoteDto noteDto = noteOperations.putNote("note1", "hi");
        assertEquals("note1", noteDto.id);
        assertEquals("hi", noteDto.text);
    }

    @Test
    @DirtiesContext
    public void canGetNote() {
        noteOperations.putNote("note1", "hi");
        NoteDto noteDto = noteOperations.getNote("note1");
        assertEquals("note1", noteDto.id);
        assertEquals("hi", noteDto.text);
    }

    @Test
    @DirtiesContext
    public void canGetAllNotes() {
        noteOperations.putNote("note1", "hi");
        noteOperations.putNote("note2", "bye");

        List<NoteDto> notes = noteOperations.getAllNotes();
        assertEquals(2, notes.size());
        assertEquals("note1", notes.get(0).id);
        assertEquals("note2", notes.get(1).id);
    }

    @Test
    @DirtiesContext
    public void canDeleteNote() {
        noteOperations.putNote("note1", "hi");
        noteOperations.deleteNote("note1");

        try {
            noteOperations.getNote("note1");
            fail();
        } catch(HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    @DirtiesContext
    public void canUpdateNote() {
        noteOperations.putNote("note1", "hi");
        noteOperations.putNote("note1", "bye");
        NoteDto note = noteOperations.getNote("note1");
        assertEquals("bye", note.text);
    }
}
