package me.loki2302.controllers;

import me.loki2302.changelog.ChangeLog;
import me.loki2302.dto.DeleteNoteDto;
import me.loki2302.dto.ErrorDto;
import me.loki2302.dto.NoteDto;
import me.loki2302.dto.NoteFieldsDto;
import me.loki2302.entities.Note;
import me.loki2302.entities.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/notes/")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ChangeLog changeLog;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object getNote(@PathVariable String id) {
        Note note = noteRepository.findOne(id);
        if(note == null) {
            ErrorDto errorDto = new ErrorDto();
            errorDto.message = "No such note";
            return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.NOT_FOUND);
        }

        NoteDto noteDto = new NoteDto();
        noteDto.id = note.id;
        noteDto.text = note.text;
        noteDto.events = changeLog.getEvents();

        return new ResponseEntity<NoteDto>(noteDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Object createNote(
            @PathVariable String id,
            @RequestBody @Valid NoteFieldsDto noteFieldsDto) {
        Note existingNote = noteRepository.findOne(id);
        if(existingNote != null) {
            ErrorDto errorDto = new ErrorDto();
            errorDto.message = "Note already exists";
            return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.CONFLICT);
        }

        Note note = new Note();
        note.id = id;
        note.text = noteFieldsDto.text;
        note = noteRepository.save(note);

        NoteDto noteDto = new NoteDto();
        noteDto.id = note.id;
        noteDto.text = note.text;
        noteDto.events = changeLog.getEvents();

        return new ResponseEntity<NoteDto>(noteDto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Object updateNote(
            @PathVariable String id,
            @RequestBody @Valid NoteFieldsDto noteFieldsDto) {

        Note note = noteRepository.findOne(id);
        if(note == null) {
            ErrorDto errorDto = new ErrorDto();
            errorDto.message = "No such note";
            return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.NOT_FOUND);
        }

        note.text = noteFieldsDto.text;

        note = noteRepository.save(note);

        NoteDto noteDto = new NoteDto();
        noteDto.id = note.id;
        noteDto.text = note.text;
        noteDto.events = changeLog.getEvents();

        return new ResponseEntity<NoteDto>(noteDto, HttpStatus.OK);
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

        DeleteNoteDto deleteNoteDto = new DeleteNoteDto();
        deleteNoteDto.events = changeLog.getEvents();

        return new ResponseEntity<DeleteNoteDto>(deleteNoteDto, HttpStatus.OK);
    }
}
