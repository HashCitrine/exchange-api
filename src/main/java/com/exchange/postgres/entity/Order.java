package com.exchange.postgres.entity;

import com.exchange.Constants;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
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

    // 미체결량만
    private Long stock;

    // 매수 매도 할때 여기 값이 필요함 (constants 타입 적기)

}
