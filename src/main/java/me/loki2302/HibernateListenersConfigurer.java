package me.loki2302;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Component
public class HibernateListenersConfigurer {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private HibernateListeners hibernateListeners;

    @PostConstruct
    public void init() {
        HibernateEntityManagerFactory hibernateEntityManagerFactory =
                (HibernateEntityManagerFactory)entityManagerFactory;
        SessionFactoryImpl sessionFactoryImpl =
                (SessionFactoryImpl)hibernateEntityManagerFactory.getSessionFactory();
        EventListenerRegistry eventListenerRegistry = sessionFactoryImpl.
                getServiceRegistry().
                getService(EventListenerRegistry.class);

        eventListenerRegistry.appendListeners(EventType.PRE_INSERT, hibernateListeners);
        eventListenerRegistry.appendListeners(EventType.PRE_UPDATE, hibernateListeners);
        eventListenerRegistry.appendListeners(EventType.PRE_DELETE, hibernateListeners);
    }
}
