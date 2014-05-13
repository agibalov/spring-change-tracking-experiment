package me.loki2302.client;

import me.loki2302.changelog.ChangeLogEvent;
import me.loki2302.changelog.CreateEntityChangeLogEvent;
import me.loki2302.changelog.DeleteEntityChangeLogEvent;
import me.loki2302.changelog.UpdateEntityChangeLogEvent;

import java.util.HashMap;
import java.util.Map;

class EntityHandlerRegistry {
    private final Map<String, EntityHandler> entityHandlers = new HashMap<String, EntityHandler>();

    public void register(String entityName, EntityHandler entityHandler) {
        if(entityHandlers.containsKey(entityName)) {
            throw new IllegalStateException("Handler for " + entityName + " already registered");
        }

        entityHandlers.put(entityName, entityHandler);
    }

    public void handle(LocalRepository<LocalNote> noteRepository, ChangeLogEvent event) {
        String entityName = event.entityName;
        EntityHandler entityHandler = entityHandlers.get(entityName);
        if(entityHandler == null) {
            throw new IllegalStateException("No handler registered for " + entityName);
        }

        if(event instanceof CreateEntityChangeLogEvent) {
            CreateEntityChangeLogEvent e = (CreateEntityChangeLogEvent)event;
            entityHandler.handleCreateEntityChangeLogEvent(noteRepository, e);
        } else if(event instanceof UpdateEntityChangeLogEvent) {
            UpdateEntityChangeLogEvent e = (UpdateEntityChangeLogEvent)event;
            entityHandler.handleUpdateEntityChangeLogEvent(noteRepository, e);
        } else if(event instanceof DeleteEntityChangeLogEvent) {
            DeleteEntityChangeLogEvent e = (DeleteEntityChangeLogEvent)event;
            entityHandler.handleDeleteEntityChangeLogEvent(noteRepository, e);
        } else {
            throw new RuntimeException("Unknown event type " + event.getClass());
        }
    }
}
