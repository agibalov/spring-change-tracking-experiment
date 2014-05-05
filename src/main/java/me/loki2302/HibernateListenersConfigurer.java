package me.loki2302;

import me.loki2302.changelog.ChangeLog;
import me.loki2302.changelog.CreateEntityChangeLogEvent;
import me.loki2302.changelog.DeleteEntityChangeLogEvent;
import me.loki2302.changelog.UpdateEntityChangeLogEvent;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.*;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Service
public class HibernateListenersConfigurer {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private ChangeLog changeLog;

    private static Map<String, Object> makePropertyMap(String[] propertyNames, Object[] propertyValues) {
        Map<String, Object> properties = new HashMap<String, Object>();
        for(int i = 0; i < propertyNames.length; ++i) {
            String propertyName = propertyNames[i];
            Object propertyValue = propertyValues[i];
            properties.put(propertyName, propertyValue);
        }
        return properties;
    }

    @PostConstruct
    public void init() {
        HibernateEntityManagerFactory hibernateEntityManagerFactory = (HibernateEntityManagerFactory) this.entityManagerFactory;
        SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) hibernateEntityManagerFactory.getSessionFactory();
        EventListenerRegistry eventListenerRegistry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);

        eventListenerRegistry.appendListeners(EventType.PRE_INSERT, new PreInsertEventListener() {
            @Override
            public boolean onPreInsert(PreInsertEvent event) {
                EntityPersister entityPersister = event.getPersister();
                String[] propertyNames = entityPersister.getPropertyNames();

                CreateEntityChangeLogEvent changeLogEvent = new CreateEntityChangeLogEvent();
                changeLogEvent.name = event.getEntityName();
                changeLogEvent.id = (String)event.getId();
                changeLogEvent.properties = makePropertyMap(propertyNames, event.getState());
                changeLog.append(changeLogEvent);

                return false;
            }
        });

        eventListenerRegistry.appendListeners(EventType.PRE_UPDATE, new PreUpdateEventListener() {
            @Override
            public boolean onPreUpdate(PreUpdateEvent event) {
                EntityPersister entityPersister = event.getPersister();
                String[] propertyNames = entityPersister.getPropertyNames();

                UpdateEntityChangeLogEvent changeLogEvent = new UpdateEntityChangeLogEvent();
                changeLogEvent.name = event.getEntityName();
                changeLogEvent.id = (String)event.getId();
                changeLogEvent.oldProperties = makePropertyMap(propertyNames, event.getOldState());
                changeLogEvent.properties = makePropertyMap(propertyNames, event.getState());
                changeLog.append(changeLogEvent);

                return false;
            }
        });

        eventListenerRegistry.appendListeners(EventType.PRE_DELETE, new PreDeleteEventListener() {
            @Override
            public boolean onPreDelete(PreDeleteEvent event) {
                DeleteEntityChangeLogEvent changeLogEvent = new DeleteEntityChangeLogEvent();
                changeLogEvent.name = event.getEntityName();
                changeLogEvent.id = (String)event.getId();
                changeLog.append(changeLogEvent);
                return false;
            }
        });
    }
}
