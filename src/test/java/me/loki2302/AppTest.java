package me.loki2302;

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

    @Test
    public void dummy() {
        String s = new RestTemplate().getForObject("http://localhost:8080/", String.class);
        assertEquals("Hello there!", s);
    }
}
