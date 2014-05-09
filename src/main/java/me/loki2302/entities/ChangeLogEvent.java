package me.loki2302.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateEntityChangeLogEvent.class, name = "create"),
        @JsonSubTypes.Type(value = UpdateEntityChangeLogEvent.class, name = "update"),
        @JsonSubTypes.Type(value = DeleteEntityChangeLogEvent.class, name = "delete")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "entityType",
        discriminatorType = DiscriminatorType.STRING
)
public abstract class ChangeLogEvent {
    @Id
    @GeneratedValue
    public Long id;

    public String entityId;
    public String entityName;
}
