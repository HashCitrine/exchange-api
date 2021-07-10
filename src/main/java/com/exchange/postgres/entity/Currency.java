package com.exchange.postgres.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Currency {

    @Id
    @Column(name = "currency")
    private String currency;

    private String currencyKr;

    private String currencyAbbr;

    private Long currentPrice;

    private Long previousPrice;

    private Long transactionPrice;
}
