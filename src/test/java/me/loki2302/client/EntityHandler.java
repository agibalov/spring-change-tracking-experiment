package me.loki2302.client;

import me.loki2302.changelog.CreateEntityChangeLogEvent;
import me.loki2302.changelog.DeleteEntityChangeLogEvent;
import me.loki2302.changelog.UpdateEntityChangeLogEvent;

public interface EntityHandler {
    void handleCreateEntityChangeLogEvent(NoteDataContext noteDataContext, CreateEntityChangeLogEvent event);
    void handleUpdateEntityChangeLogEvent(NoteDataContext noteDataContext, UpdateEntityChangeLogEvent event);
    void handleDeleteEntityChangeLogEvent(NoteDataContext noteDataContext, DeleteEntityChangeLogEvent event);
}
