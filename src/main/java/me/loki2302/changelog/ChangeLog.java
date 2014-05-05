package me.loki2302.changelog;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChangeLog {
    private List<ChangeLogEvent> events = new ArrayList<ChangeLogEvent>();

    public void append(ChangeLogEvent changeLogEvent) {
        events.add(changeLogEvent);
    }

    public List<ChangeLogEvent> getEvents() {
        return events;
    }
}
