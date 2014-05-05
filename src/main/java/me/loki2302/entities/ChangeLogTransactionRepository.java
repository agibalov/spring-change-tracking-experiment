package me.loki2302.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeLogTransactionRepository extends JpaRepository<ChangeLogTransaction, Long> {
}
