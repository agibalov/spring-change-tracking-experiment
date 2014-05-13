package me.loki2302.client;

import me.loki2302.changelog.CreateEntityChangeLogEvent;
import me.loki2302.changelog.DeleteEntityChangeLogEvent;
import me.loki2302.changelog.UpdateEntityChangeLogEvent;

public class NoteEntityHandler implements EntityHandler {
    @Override
    public void handleCreateEntityChangeLogEvent(NoteDataContext noteDataContext, CreateEntityChangeLogEvent event) {
        LocalNote note = new LocalNote();
        note.id = event.entityId;
        note.text = (String)event.properties.get("text");
        note.text2 = (String)event.properties.get("text2");
        noteDataContext.noteRepository.save(note);
    }

    @Override
    public void handleUpdateEntityChangeLogEvent(NoteDataContext noteDataContext, UpdateEntityChangeLogEvent event) {
        LocalNote note = new LocalNote();
        note.id = event.entityId;
        note.text = (String)event.properties.get("text");
        note.text2 = (String)event.properties.get("text2");
        noteDataContext.noteRepository.save(note);
    }

    @Override
    public void handleDeleteEntityChangeLogEvent(NoteDataContext noteDataContext, DeleteEntityChangeLogEvent event) {
        noteDataContext.noteRepository.delete(event.entityId);
    }
}
