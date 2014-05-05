package me.loki2302.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("update")
public class UpdateEntityChangeLogEvent extends ChangeLogEvent {
    @ElementCollection(fetch = FetchType.EAGER)
    public List<PropertyInfo> oldProperties;

    @ElementCollection(fetch = FetchType.EAGER)
    public List<PropertyInfo> properties;

}
