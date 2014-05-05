package me.loki2302;

import me.loki2302.changelog.ChangeLog;
import me.loki2302.entities.*;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.*;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

@Service
public class HibernateListenersConfigurer {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private ChangeLog changeLog;

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

    @PostConstruct
    public void init() {
        HibernateEntityManagerFactory hibernateEntityManagerFactory = (HibernateEntityManagerFactory) this.entityManagerFactory;
        SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) hibernateEntityManagerFactory.getSessionFactory();
        EventListenerRegistry eventListenerRegistry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);

        eventListenerRegistry.appendListeners(EventType.PRE_INSERT, new PreInsertEventListener() {
            @Override
            public boolean onPreInsert(PreInsertEvent event) {
                if(!shouldProcessEntity(event.getEntity())) {
                    return false;
                }

                EntityPersister entityPersister = event.getPersister();
                String[] propertyNames = entityPersister.getPropertyNames();

                CreateEntityChangeLogEvent changeLogEvent = new CreateEntityChangeLogEvent();
                changeLogEvent.name = event.getEntityName();
                changeLogEvent.id = (String)event.getId();
                changeLogEvent.properties = makePropertyInfoList(propertyNames, event.getState());
                changeLog.append(changeLogEvent);

                return false;
            }
        });

        eventListenerRegistry.appendListeners(EventType.PRE_UPDATE, new PreUpdateEventListener() {
            @Override
            public boolean onPreUpdate(PreUpdateEvent event) {
                if(!shouldProcessEntity(event.getEntity())) {
                    return false;
                }

                EntityPersister entityPersister = event.getPersister();
                String[] propertyNames = entityPersister.getPropertyNames();

                UpdateEntityChangeLogEvent changeLogEvent = new UpdateEntityChangeLogEvent();
                changeLogEvent.name = event.getEntityName();
                changeLogEvent.id = (String)event.getId();
                changeLogEvent.oldProperties = makePropertyInfoList(propertyNames, event.getOldState());
                changeLogEvent.properties = makePropertyInfoList(propertyNames, event.getState());
                changeLog.append(changeLogEvent);

                return false;
            }
        });

        eventListenerRegistry.appendListeners(EventType.PRE_DELETE, new PreDeleteEventListener() {
            @Override
            public boolean onPreDelete(PreDeleteEvent event) {
                if(!shouldProcessEntity(event.getEntity())) {
                    return false;
                }

                DeleteEntityChangeLogEvent changeLogEvent = new DeleteEntityChangeLogEvent();
                changeLogEvent.name = event.getEntityName();
                changeLogEvent.id = (String)event.getId();
                changeLog.append(changeLogEvent);
                return false;
            }
        });
    }
}
