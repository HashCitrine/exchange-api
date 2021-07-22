package com.exchange.postgres.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Wallet {

    @Id
    private String memberId;

    private String currency;

    private Long quantity;

    private Long averagePrice;
}
