package me.loki2302.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("create")
public class CreateEntityChangeLogEvent extends ChangeLogEvent {
    @ElementCollection(fetch = FetchType.EAGER)
    public List<PropertyInfo> properties;
}
