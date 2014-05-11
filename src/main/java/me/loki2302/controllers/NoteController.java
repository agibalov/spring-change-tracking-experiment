package me.loki2302.controllers;

import me.loki2302.dto.ErrorDto;
import me.loki2302.dto.NoteDto;
import me.loki2302.dto.NoteFieldsDto;
import me.loki2302.entities.Note;
import me.loki2302.entities.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/notes/")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteMapper noteMapper;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        List<NoteDto> noteDtos = noteMapper.makeNoteDtos(notes);
        return new ResponseEntity<List<NoteDto>>(noteDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object getNote(@PathVariable String id) {
        Note note = noteRepository.findOne(id);
        if (note == null) {
            ErrorDto errorDto = new ErrorDto();
            errorDto.message = "No such note";
            return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.NOT_FOUND);
        }

        NoteDto noteDto = noteMapper.makeNoteDto(note);
        return new ResponseEntity<NoteDto>(noteDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Object createOrUpdateNote(
            @PathVariable String id,
            @RequestBody @Valid NoteFieldsDto noteFieldsDto) {

        Note note = noteRepository.findOne(id);
        boolean hasCreatedNew = false;
        if (note == null) {
            note = new Note();
            note.id = id;
            hasCreatedNew = true;
        }

        note.text = noteFieldsDto.text;
        note = noteRepository.save(note);

        NoteDto noteDto = noteMapper.makeNoteDto(note);
        return new ResponseEntity<NoteDto>(noteDto, hasCreatedNew ? HttpStatus.CREATED : HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object deleteNote(@PathVariable String id) {
        Note note = noteRepository.findOne(id);
        if(note == null) {
            ErrorDto errorDto = new ErrorDto();
            errorDto.message = "No such note";
            return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.NOT_FOUND);
        }

        noteRepository.delete(id);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
