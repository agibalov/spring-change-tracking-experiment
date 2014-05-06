package me.loki2302.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChangeLogTransactionRepository extends JpaRepository<ChangeLogTransaction, Long> {
    List<ChangeLogTransaction> findByIdGreaterThan(long firstId);
}
