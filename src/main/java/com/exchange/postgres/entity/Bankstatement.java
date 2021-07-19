package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Bankstatement {

    @Id @GeneratedValue
    private Long transactionId;
    private LocalDateTime transactionDate;
    private String memberId;
    @Enumerated(EnumType.STRING)
    private Constants.TRANSACTION_TYPE transactionType;
    private Long krw;
}
