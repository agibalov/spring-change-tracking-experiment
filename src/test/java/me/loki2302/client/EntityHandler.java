package me.loki2302.client;

import me.loki2302.changelog.CreateEntityChangeLogEvent;
import me.loki2302.changelog.DeleteEntityChangeLogEvent;
import me.loki2302.changelog.UpdateEntityChangeLogEvent;

public interface EntityHandler {
    void handleCreateEntity(NoteDataContext dataContext, CreateEntityChangeLogEvent event);
    void handleUpdateEntity(NoteDataContext dataContext, UpdateEntityChangeLogEvent event);
    void handleDeleteEntity(NoteDataContext dataContext, DeleteEntityChangeLogEvent event);
}
