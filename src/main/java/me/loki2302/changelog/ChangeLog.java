package me.loki2302.changelog;

import me.loki2302.entities.ChangeLogEvent;
import me.loki2302.entities.ChangeLogEventRepository;
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
    private ChangeLogEventRepository changeLogEventRepository;

    public void append(ChangeLogEvent changeLogEvent) {
        events.add(changeLogEvent);
    }

    public List<ChangeLogEvent> getEvents() {
        return events;
    }

    public void saveLog() {
        System.out.println("SAVE LOG!!!");
        if(events.isEmpty()) {
            return;
        }

        ChangeLogEvent changeLogEvent = events.get(0);
        events = changeLogEventRepository.save(events);
        ChangeLogTransaction changeLogTransaction = new ChangeLogTransaction();

        changeLogTransaction.description = String.format(
                "Event: id=%s, name=%s",
                changeLogEvent.id,
                changeLogEvent.name);
        changeLogTransaction.events = events;
        changeLogTransaction = changeLogTransactionRepository.save(changeLogTransaction);

        System.out.println(changeLogTransaction);
    }
}
