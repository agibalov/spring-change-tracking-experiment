package me.loki2302.entities;

import org.springframework.boot.autoconfigure.data.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, String> {

}
