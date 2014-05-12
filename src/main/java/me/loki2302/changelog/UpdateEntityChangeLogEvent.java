package me.loki2302.changelog;

import java.util.Map;

public class UpdateEntityChangeLogEvent extends ChangeLogEvent {
    public Map<String, Object> oldProperties;
    public Map<String, Object> properties;
}
