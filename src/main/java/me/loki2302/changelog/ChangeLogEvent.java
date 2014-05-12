package me.loki2302.changelog;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateEntityChangeLogEvent.class, name = "create"),
        @JsonSubTypes.Type(value = UpdateEntityChangeLogEvent.class, name = "update"),
        @JsonSubTypes.Type(value = DeleteEntityChangeLogEvent.class, name = "delete")
})
public abstract class ChangeLogEvent {
    public String entityId;
    public String entityName;
}
