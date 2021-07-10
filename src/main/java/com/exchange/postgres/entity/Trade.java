package com.exchange.postgres.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Trade {

    @Id
    private Long tradeId;

    private LocalDateTime tradeDate;

    private Long buyOrderId;

    private Long sellOrderId;
}
