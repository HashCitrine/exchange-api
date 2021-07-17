package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WalletRepository extends JpaRepository<Wallet, String> {
    Wallet findByMemberId(String memberId);
}
