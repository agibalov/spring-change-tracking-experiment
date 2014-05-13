package me.loki2302.client.commands;

import me.loki2302.client.LocalNote;
import me.loki2302.client.NoteDataContext;
import me.loki2302.client.api.NoteOperations;

public class SaveNoteCommand implements ApiCommand<LocalNote> {
    public String id;
    public String text;
    public String text2;

    @Override
    public LocalNote applyLocally(NoteDataContext noteDataContext) {
        LocalNote localNote = new LocalNote();
        localNote.id = id;
        localNote.text = text;
        localNote.text2 = text2;
        return noteDataContext.noteRepository.save(localNote);
    }

    @Override
    public void applyRemotely(NoteOperations noteOperations) {
        noteOperations.putNote(id, text);
    }
}
