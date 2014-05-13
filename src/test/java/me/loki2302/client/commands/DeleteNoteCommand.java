package me.loki2302.client.commands;

import me.loki2302.client.NoteDataContext;
import me.loki2302.client.api.NoteOperations;

public class DeleteNoteCommand implements ApiCommand<Void> {
    public String id;

    @Override
    public Void applyLocally(NoteDataContext noteDataContext) {
        noteDataContext.noteRepository.delete(id);
        return null;
    }

    @Override
    public void applyRemotely(NoteOperations noteOperations) {
        noteOperations.deleteNote(id);
    }
}
