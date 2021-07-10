package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter @Setter
public class Order {

    @Id
    private Long orderId;

    private Date orderDate;

    private String orderMember;

    private String currency;

    private Constants.ROLE role;
}
