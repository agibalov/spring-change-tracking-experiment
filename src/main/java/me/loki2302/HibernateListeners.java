package me.loki2302;

import me.loki2302.changelog.ChangeLog;
import me.loki2302.entities.*;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        changeLogEvent.properties = makePropertyInfoList(propertyNames, event.getState());
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
        changeLogEvent.oldProperties = makePropertyInfoList(propertyNames, event.getOldState());
        changeLogEvent.properties = makePropertyInfoList(propertyNames, event.getState());
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

    private static List<PropertyInfo> makePropertyInfoList(String[] propertyNames, Object[] propertyValues) {
        List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();
        for(int i = 0; i < propertyNames.length; ++i) {
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.name = propertyNames[i];
            propertyInfo.value = String.valueOf(propertyValues[i]);
            propertyInfoList.add(propertyInfo);
        }
        return propertyInfoList;
    }

    private static boolean shouldProcessEntity(Object entity) {
        return entity instanceof Note;
    }
}
