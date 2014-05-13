package me.loki2302.client;

import me.loki2302.NoteOperations;

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
