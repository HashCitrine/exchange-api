package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Bankstatement {

    @Id
    private Long transactionId;
    private LocalDateTime transactionDate;
    private String memberId;
    @Enumerated(EnumType.STRING)
    private Constants.TRANSACTION_TYPE transactionType;
    private String bank;
    private Long krw;
}
