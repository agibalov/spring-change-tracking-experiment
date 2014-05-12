package me.loki2302.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class ChangeLogTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Lob
    @Column(length = 1024 * 1024)
    public String changeLogEventsJson;
}
