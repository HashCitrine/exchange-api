package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "order")
public class Order {

    @Id
    private Long orderId;

    private LocalDateTime orderDate;

    private String orderMember;

    private String currency;

    @Enumerated(EnumType.STRING)
    private Constants.ORDER_TYPE orderType;

    private Long price;

    private Long quantity;

    private Long tradeId;

}
