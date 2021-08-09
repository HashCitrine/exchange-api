package com.exchange.postgres.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Currency {

    @Id
    private String currency;

/*    private String currencyKr;

    private String currencyAbbr;*/

    private Long currentPrice;

    private Long previousPrice;

    private Long transactionPrice;
}
