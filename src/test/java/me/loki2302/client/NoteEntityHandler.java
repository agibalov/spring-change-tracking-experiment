package me.loki2302.client;

import me.loki2302.changelog.CreateEntityChangeLogEvent;
import me.loki2302.changelog.DeleteEntityChangeLogEvent;
import me.loki2302.changelog.UpdateEntityChangeLogEvent;

public class NoteEntityHandler implements EntityHandler {
    @Override
    public void handleCreateEntityChangeLogEvent(LocalRepository<LocalNote> noteRepository, CreateEntityChangeLogEvent event) {
        LocalNote note = new LocalNote();
        note.id = event.entityId;
        note.text = (String)event.properties.get("text");
        note.text2 = (String)event.properties.get("text2");
        noteRepository.save(note);
    }

    @Override
    public void handleUpdateEntityChangeLogEvent(LocalRepository<LocalNote> noteRepository, UpdateEntityChangeLogEvent event) {
        LocalNote note = new LocalNote();
        note.id = event.entityId;
        note.text = (String)event.properties.get("text");
        note.text2 = (String)event.properties.get("text2");
        noteRepository.save(note);
    }

    @Override
    public void handleDeleteEntityChangeLogEvent(LocalRepository<LocalNote> noteRepository, DeleteEntityChangeLogEvent event) {
        noteRepository.delete(event.entityId);
    }
}
