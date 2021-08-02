package com.exchange.postgres.repository;

import com.exchange.postgres.entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
    BankStatement findByMemberId(String memberId);
}
