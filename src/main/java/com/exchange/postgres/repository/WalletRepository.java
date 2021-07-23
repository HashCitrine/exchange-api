package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WalletRepository extends JpaRepository<Wallet, String> {
    Wallet findByMemberId(String memberId);

    @Modifying
    @Query(value = "update wallet set quantity = quantity + ?1 where member_id=?2", nativeQuery = true)
    void updateWallet(Long quantity, String memberId);
}
