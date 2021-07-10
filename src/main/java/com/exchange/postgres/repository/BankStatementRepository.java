package com.exchange.postgres.repository;

import com.exchange.postgres.entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
}
