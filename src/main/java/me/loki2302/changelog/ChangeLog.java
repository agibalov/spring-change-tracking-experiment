package me.loki2302.changelog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.loki2302.entities.ChangeLogTransaction;
import me.loki2302.entities.ChangeLogTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChangeLog {
    private List<ChangeLogEvent> events = new ArrayList<ChangeLogEvent>();

    @Autowired
    private ChangeLogTransactionRepository changeLogTransactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void append(ChangeLogEvent changeLogEvent) {
        events.add(changeLogEvent);
    }

    public void saveLog() {
        System.out.println("SAVE LOG!!!");
        if(events.isEmpty()) {
            return;
        }

        String eventsJson;
        try {
            eventsJson = objectMapper
                    .writerWithType(new TypeReference<List<ChangeLogEvent>>() {})
                    .writeValueAsString(events);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ChangeLogTransaction changeLogTransaction = new ChangeLogTransaction();
        changeLogTransaction.changeLogEventsJson = eventsJson;
        changeLogTransactionRepository.save(changeLogTransaction);
    }
}
