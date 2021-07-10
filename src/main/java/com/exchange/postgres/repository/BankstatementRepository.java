package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Bankstatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankstatementRepository extends JpaRepository<Bankstatement, Long> {
}
