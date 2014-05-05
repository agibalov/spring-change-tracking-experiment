package me.loki2302;

import me.loki2302.entities.ChangeLogEvent;
import me.loki2302.entities.CreateEntityChangeLogEvent;
import me.loki2302.dto.NoteDto;
import me.loki2302.dto.NoteFieldsDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Config.class)
@WebAppConfiguration
@IntegrationTest
public class AppTest {
    @Test
    @DirtiesContext
    public void canCreateNote() {
        NoteFieldsDto noteFieldsDto = new NoteFieldsDto();
        noteFieldsDto.text = "hello";

        RestTemplate restTemplate = new RestTemplate();
        NoteDto noteDto = restTemplate.postForObject("http://localhost:8080/notes/123", noteFieldsDto, NoteDto.class);
        assertNotNull(noteDto);
        assertEquals("hello", noteDto.text);
        assertFalse(noteDto.id == null || noteDto.id.isEmpty());

        List<ChangeLogEvent> changeLogEvents = noteDto.events;
        assertEquals(1, changeLogEvents.size());
        assertTrue(changeLogEvents.get(0) instanceof CreateEntityChangeLogEvent);
        CreateEntityChangeLogEvent createEntityChangeLogEvent = (CreateEntityChangeLogEvent)changeLogEvents.get(0);
        assertEquals(noteDto.id, createEntityChangeLogEvent.id);
        assertEquals("me.loki2302.entities.Note", createEntityChangeLogEvent.name);
        assertEquals(2, createEntityChangeLogEvent.properties.size());
        //assertEquals(noteDto.text, createEntityChangeLogEvent.properties.get("text"));
        //assertNull(createEntityChangeLogEvent.properties.get("text2"));
    }

    @Test
    @DirtiesContext
    public void cantCreateNoteWithTheSameId() {
        NoteFieldsDto noteFieldsDto = new NoteFieldsDto();
        noteFieldsDto.text = "hello";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://localhost:8080/notes/123", noteFieldsDto, NoteDto.class);

        try {
            restTemplate.postForObject("http://localhost:8080/notes/123", noteFieldsDto, NoteDto.class);
            fail();
        } catch(HttpClientErrorException e) {
            assertEquals(HttpStatus.CONFLICT, e.getStatusCode());
        }
    }
}
