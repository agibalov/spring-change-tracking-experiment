package me.loki2302;

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
        NoteDto noteDto = noteOperations.createNote("note1", "hi");
        assertEquals("note1", noteDto.id);
        assertEquals("hi", noteDto.text);
    }

    @Test
    @DirtiesContext
    public void cantCreateNoteWithTheSameId() {
        noteOperations.createNote("note1", "hi");

        try {
            noteOperations.createNote("note1", "hi");
            fail();
        } catch(HttpClientErrorException e) {
            assertEquals(HttpStatus.CONFLICT, e.getStatusCode());
        }
    }

    @Test
    @DirtiesContext
    public void canGetNote() {
        noteOperations.createNote("note1", "hi");
        NoteDto noteDto = noteOperations.getNote("note1");
        assertEquals("note1", noteDto.id);
        assertEquals("hi", noteDto.text);
    }
}
