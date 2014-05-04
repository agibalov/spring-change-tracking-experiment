package me.loki2302;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChangeLog {
    private List<App.ChangeLogEvent> events = new ArrayList<App.ChangeLogEvent>();

    public void append(App.ChangeLogEvent changeLogEvent) {
        events.add(changeLogEvent);
    }

    public List<App.ChangeLogEvent> getEvents() {
        return events;
    }
}
