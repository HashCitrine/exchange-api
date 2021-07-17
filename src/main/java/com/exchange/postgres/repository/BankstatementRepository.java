package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BankstatementRepository extends JpaRepository<Bankstatement, Long> {
    Bankstatement findByMemberId(String memberId);
}
