package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BankstatementRepository extends JpaRepository<Bankstatement, Long> {

    @Query(value = "select b from Bankstatement b where member_id=?1", nativeQuery = true)
    Bankstatement findByMemberId(String memberId);
}
