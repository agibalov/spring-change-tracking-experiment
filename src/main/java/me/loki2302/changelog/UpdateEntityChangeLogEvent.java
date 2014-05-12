package me.loki2302.changelog;

import java.util.List;

public class UpdateEntityChangeLogEvent extends ChangeLogEvent {
    public List<PropertyInfo> oldProperties;
    public List<PropertyInfo> properties;
}
