package com.exchange.postgres.repository;

import com.exchange.postgres.entity.BankStatement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {

    @Query(value = "SELECT NEXTVAL('bank_statement_transaction_id_seq')", nativeQuery = true)
    Long getNextId();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO bank_statement(transaction_id, transaction_date, member_id, transaction_type, krw, status, upt_date) " +
            "VALUES(:#{#bankStatement.transactionId}, Now(), ' ', :#{#transactionType}, :#{#bankStatement.krw}, :#{#status}, Now())", nativeQuery = true)
    void insertBankStatement(@Param("bankStatement") BankStatement bankStatement, @Param("transactionType") String transactionType, @Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bank_statement SET status = :#{#status}, upt_date = Now() WHERE transaction_id = :#{#bankStatement.transactionId}", nativeQuery = true)
    void updateBankStatement(@Param("bankStatement") BankStatement bankStatement, @Param("status") String status);

    @Query(value = "SELECT count(*) FROM bank_statement WHERE transaction_id = ?1", nativeQuery = true)
    int checkDuplicateId(Long id);
}
