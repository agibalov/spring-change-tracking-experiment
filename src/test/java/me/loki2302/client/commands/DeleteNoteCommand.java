package me.loki2302.client.commands;

import me.loki2302.NoteOperations;
import me.loki2302.client.LocalNote;
import me.loki2302.client.LocalRepository;

public class DeleteNoteCommand implements ApiCommand<Void> {
    public String id;

    @Override
    public Void applyLocally(LocalRepository<LocalNote> noteRepository) {
        noteRepository.delete(id);
        return null;
    }

    @Override
    public void applyRemotely(NoteOperations noteOperations) {
        noteOperations.deleteNote(id);
    }
}
