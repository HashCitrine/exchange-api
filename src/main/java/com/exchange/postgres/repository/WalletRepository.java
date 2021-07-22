package com.exchange.postgres.repository;

import com.exchange.Constants;
import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WalletRepository extends JpaRepository<Wallet, String> {
    Wallet findByMemberId(String memberId);

    @Query(value = "update wallet set krw=?1, transaction_type=?2 where member_id=?3", nativeQuery = true)
    Wallet updateWallet(Long krw, Constants.TRANSACTION_TYPE transaction_type, String memberId);
}
