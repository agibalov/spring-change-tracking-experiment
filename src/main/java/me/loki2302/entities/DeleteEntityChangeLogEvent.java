package me.loki2302.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("delete")
public class DeleteEntityChangeLogEvent extends ChangeLogEvent {
}
