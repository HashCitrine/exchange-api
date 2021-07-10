package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class BankStatement {

    @Id
    private Long transactionId;
    private LocalDateTime transactionDate;
    private String memberId;
    private Constants.TRANSACTION_TYPE transactionType;
    private String bank;
    private Long krw;
}
