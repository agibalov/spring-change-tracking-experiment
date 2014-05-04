package me.loki2302;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.*;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.*;

public class App {
    public static void main(String[] args) {
        SpringApplication.run(Config.class, args);
    }

    @Configuration
    @ComponentScan
    @EnableAutoConfiguration
    @EnableTransactionManagement
    @EntityScan(basePackages = "me.loki2302")
    @EnableJpaRepositories(basePackages = "me.loki2302")
    public static class Config {
        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper;
        }

        @Bean
        public DataSource dataSource() throws SQLException {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.HSQL)
                    .build();
        }

    }

    @RestController
    public static class HomeController {
        @RequestMapping(value = "/", method = RequestMethod.GET)
        public String hello() {
            return "Hello there!";
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = CreateEntityChangeLogEvent.class, name = "create"),
            @JsonSubTypes.Type(value = UpdateEntityChangeLogEvent.class, name = "update"),
            @JsonSubTypes.Type(value = DeleteEntityChangeLogEvent.class, name = "delete")
    })
    public static abstract class ChangeLogEvent {
        public String id;
        public String name;
    }

    public static class CreateEntityChangeLogEvent extends ChangeLogEvent {
        public Map<String, Object> properties;
    }

    public static class UpdateEntityChangeLogEvent extends ChangeLogEvent {
        public Map<String, Object> oldProperties;
        public Map<String, Object> properties;
    }

    public static class DeleteEntityChangeLogEvent extends ChangeLogEvent {
    }

    @Service
    public static class HibernateListenersConfigurer {
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

    @RestController
    @RequestMapping("/notes/")
    public static class NoteController {
        @Autowired
        private NoteRepository noteRepository;

        @Autowired
        private ChangeLog changeLog;

        @RequestMapping(value = "/{id}", method = RequestMethod.GET)
        public Object getNote(@PathVariable String id) {
            Note note = noteRepository.findOne(id);
            if(note == null) {
                ErrorDto errorDto = new ErrorDto();
                errorDto.message = "No such note";
                return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.NOT_FOUND);
            }

            NoteDto noteDto = new NoteDto();
            noteDto.id = note.id;
            noteDto.text = note.text;
            noteDto.events = changeLog.getEvents();

            return new ResponseEntity<NoteDto>(noteDto, HttpStatus.OK);
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.POST)
        public Object createNote(
                @PathVariable String id,
                @RequestBody @Valid NoteFieldsDto noteFieldsDto) {
            Note existingNote = noteRepository.findOne(id);
            if(existingNote != null) {
                ErrorDto errorDto = new ErrorDto();
                errorDto.message = "Note already exists";
                return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.CONFLICT);
            }

            Note note = new Note();
            note.id = id;
            note.text = noteFieldsDto.text;
            note = noteRepository.save(note);

            NoteDto noteDto = new NoteDto();
            noteDto.id = note.id;
            noteDto.text = note.text;
            noteDto.events = changeLog.getEvents();

            return new ResponseEntity<NoteDto>(noteDto, HttpStatus.CREATED);
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
        public Object updateNote(
                @PathVariable String id,
                @RequestBody @Valid NoteFieldsDto noteFieldsDto) {

            Note note = noteRepository.findOne(id);
            if(note == null) {
                ErrorDto errorDto = new ErrorDto();
                errorDto.message = "No such note";
                return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.NOT_FOUND);
            }

            note.text = noteFieldsDto.text;

            note = noteRepository.save(note);

            NoteDto noteDto = new NoteDto();
            noteDto.id = note.id;
            noteDto.text = note.text;
            noteDto.events = changeLog.getEvents();

            return new ResponseEntity<NoteDto>(noteDto, HttpStatus.OK);
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
        public Object deleteNote(@PathVariable String id) {
            Note note = noteRepository.findOne(id);
            if(note == null) {
                ErrorDto errorDto = new ErrorDto();
                errorDto.message = "No such note";
                return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.NOT_FOUND);
            }

            noteRepository.delete(id);

            return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
        }
    }

    public static class ErrorDto {
        public String message;
    }

    @ControllerAdvice
    public static class ExceptionHandlerControllerAdvice {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public Map<String, String> handle(MethodArgumentNotValidException e) {
            Map<String, String> errorsMap = new HashMap<String, String>();
            List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
            for(FieldError fieldError : fieldErrorList) {
                String fieldName = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                errorsMap.put(fieldName, message);
            }

            System.out.println("VALIDATION ERROR!");
            return errorsMap;
        }
    }

    public static class NoteFieldsDto {
        @NotEmpty
        @Length(max = 5)
        public String text;
    }

    public static class NoteDto extends NoteFieldsDto {
        public String id;
        public List<ChangeLogEvent> events;
    }
}
