package me.loki2302;

import me.loki2302.dto.NoteDto;
import me.loki2302.dto.NoteFieldsDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class NoteOperations {
    private final RestTemplate restTemplate;

    public NoteOperations(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public NoteDto createNote(String id, String text) {
        NoteFieldsDto noteFieldsDto = new NoteFieldsDto();
        noteFieldsDto.text = text;

        NoteDto noteDto = restTemplate.postForObject(
                buildUri("notes/{id}", id),
                noteFieldsDto,
                NoteDto.class);

        return noteDto;
    }

    public NoteDto getNote(String id) {
        return restTemplate.getForObject(buildUri("notes/{id}", id), NoteDto.class);
    }

    public List<NoteDto> getAllNotes() {
        ResponseEntity<List<NoteDto>> responseEntity = restTemplate.exchange(
                buildUri("notes"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<NoteDto>>() {});

        return responseEntity.getBody();
    }

    public NoteDto updateNote(String id, String text) {
        NoteFieldsDto noteFieldsDto = new NoteFieldsDto();
        noteFieldsDto.text = text;

        ResponseEntity<NoteDto> responseEntity = restTemplate.exchange(
                buildUri("notes/{id}", id),
                HttpMethod.PUT,
                new HttpEntity<NoteFieldsDto>(noteFieldsDto),
                NoteDto.class);

        return responseEntity.getBody();
    }

    public void deleteNote(String id) {
        restTemplate.delete(buildUri("notes/{id}", id));
    }

    private static String buildUri(String path, Object... vars) {
        return UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .path(path)
                .buildAndExpand(vars)
                .toUriString();
    }
}
