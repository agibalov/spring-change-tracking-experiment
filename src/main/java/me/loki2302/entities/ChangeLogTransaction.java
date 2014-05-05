package me.loki2302.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ChangeLogTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String description;
}
