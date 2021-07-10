package com.exchange.postgres.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Wallet {

    @Id
    private String memberId;

    private String currency;

    private Long quantity;

    private Long averagePrice;
}
