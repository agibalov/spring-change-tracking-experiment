package me.loki2302;

import me.loki2302.changelog.ChangeLog;
import me.loki2302.changelog.CreateEntityChangeLogEvent;
import me.loki2302.changelog.DeleteEntityChangeLogEvent;
import me.loki2302.changelog.UpdateEntityChangeLogEvent;
import me.loki2302.entities.Note;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HibernateListeners implements PreInsertEventListener, PreUpdateEventListener, PreDeleteEventListener {
    @Autowired
    private ChangeLog changeLog;

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        if(!shouldProcessEntity(event.getEntity())) {
            return false;
        }

        EntityPersister entityPersister = event.getPersister();
        String[] propertyNames = entityPersister.getPropertyNames();

        CreateEntityChangeLogEvent changeLogEvent = new CreateEntityChangeLogEvent();
        changeLogEvent.entityName = event.getEntityName();
        changeLogEvent.entityId = (String)event.getId();
        changeLogEvent.properties = makePropertyMap(propertyNames, event.getState());
        changeLog.append(changeLogEvent);

        return false;
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        if(!shouldProcessEntity(event.getEntity())) {
            return false;
        }

        EntityPersister entityPersister = event.getPersister();
        String[] propertyNames = entityPersister.getPropertyNames();

        UpdateEntityChangeLogEvent changeLogEvent = new UpdateEntityChangeLogEvent();
        changeLogEvent.entityName = event.getEntityName();
        changeLogEvent.entityId = (String)event.getId();
        changeLogEvent.oldProperties = makePropertyMap(propertyNames, event.getOldState());
        changeLogEvent.properties = makePropertyMap(propertyNames, event.getState());
        changeLog.append(changeLogEvent);

        return false;
    }

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        if(!shouldProcessEntity(event.getEntity())) {
            return false;
        }

        DeleteEntityChangeLogEvent changeLogEvent = new DeleteEntityChangeLogEvent();
        changeLogEvent.entityName = event.getEntityName();
        changeLogEvent.entityId = (String)event.getId();
        changeLog.append(changeLogEvent);

        return false;
    }

    private static Map<String, Object> makePropertyMap(String[] propertyNames, Object[] propertyValues) {
        Map<String, Object> propertyMap = new HashMap<String, Object>();
        for(int i = 0; i < propertyNames.length; ++i) {
            String propertyName = propertyNames[i];
            Object propertyValue = propertyValues[i];
            propertyMap.put(propertyName, propertyValue);
        }
        return propertyMap;
    }

    private static boolean shouldProcessEntity(Object entity) {
        return entity instanceof Note;
    }
}
