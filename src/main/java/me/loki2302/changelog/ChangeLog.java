package me.loki2302.changelog;

import me.loki2302.entities.ChangeLogTransaction;
import me.loki2302.entities.ChangeLogTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChangeLog {
    private List<ChangeLogEvent> events = new ArrayList<ChangeLogEvent>();

    @Autowired
    private ChangeLogTransactionRepository changeLogTransactionRepository;

    public void append(ChangeLogEvent changeLogEvent) {
        events.add(changeLogEvent);
    }

    public List<ChangeLogEvent> getEvents() {
        return events;
    }

    @PreDestroy
    public void saveLog() {
        System.out.println("PREDESTROY!!!");
        ChangeLogEvent changeLogEvent = events.get(0);

        ChangeLogTransaction changeLogTransaction = new ChangeLogTransaction();
        changeLogTransaction.description = String.format(
                "Event: id=%s, name=%s",
                changeLogEvent.id,
                changeLogEvent.name);
        changeLogTransactionRepository.save(changeLogTransaction);
    }
}
