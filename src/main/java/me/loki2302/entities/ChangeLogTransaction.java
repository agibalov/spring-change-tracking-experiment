package me.loki2302.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class ChangeLogTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String description;

    @Lob
    public String changeLogEventsJson;

}
