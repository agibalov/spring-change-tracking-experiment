package me.loki2302.client;

import me.loki2302.NoteOperations;

public class SaveNoteCommand implements ApiCommand<LocalNote> {
    public String id;
    public String text;
    public String text2;

    @Override
    public LocalNote applyLocally(LocalRepository<LocalNote> noteRepository) {
        LocalNote localNote = new LocalNote();
        localNote.id = id;
        localNote.text = text;
        localNote.text2 = text2;
        return noteRepository.save(localNote);
    }

    @Override
    public void applyRemotely(NoteOperations noteOperations) {
        noteOperations.putNote(id, text);
    }
}
