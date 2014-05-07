package me.loki2302.controllers;

import me.loki2302.dto.NoteDto;
import me.loki2302.entities.Note;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NoteMapper {
    public NoteDto makeNoteDto(Note note) {
        NoteDto noteDto = new NoteDto();
        noteDto.id = note.id;
        noteDto.text = note.text;
        return noteDto;
    }

    public List<NoteDto> makeNoteDtos(List<Note> notes) {
        List<NoteDto> noteDtoList = new ArrayList<NoteDto>();
        for(Note note : notes) {
            NoteDto noteDto = makeNoteDto(note);
            noteDtoList.add(noteDto);
        }
        return noteDtoList;
    }
}
