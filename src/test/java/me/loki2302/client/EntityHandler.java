package me.loki2302.client;

import me.loki2302.changelog.CreateEntityChangeLogEvent;
import me.loki2302.changelog.DeleteEntityChangeLogEvent;
import me.loki2302.changelog.UpdateEntityChangeLogEvent;

public interface EntityHandler {
    void handleCreateEntityChangeLogEvent(LocalRepository<LocalNote> noteRepository, CreateEntityChangeLogEvent event);
    void handleUpdateEntityChangeLogEvent(LocalRepository<LocalNote> noteRepository, UpdateEntityChangeLogEvent event);
    void handleDeleteEntityChangeLogEvent(LocalRepository<LocalNote> noteRepository, DeleteEntityChangeLogEvent event);
}
