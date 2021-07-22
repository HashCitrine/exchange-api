package com.exchange.postgres.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Trade {

    @Id
    private Long tradeId;

    private LocalDateTime tradeDate;

    private Long buyOrderId;

    private Long sellOrderId;

    private Long quantity;
}
