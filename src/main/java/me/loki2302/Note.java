package me.loki2302;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

@Entity
public class Note {
    @Id
    public String id;
    public String text;
    public String text2;

    @Override
    public String toString() {
        return String.format("Note(%s,%s)", id, text);
    }
}
