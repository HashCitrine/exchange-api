package com.exchange.postgres.repository;

import com.exchange.Constants;
import com.exchange.postgres.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Modifying
    @Query(value = "insert into \"order\" (order_date, order_member, currency, order_type, price, quantity, stock) " +
            "values (now(), ?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void insertOrder(String orderMember,
                      String currency,
                      String orderType,
                      Long price,
                      Long quantity,
                      Long stock);
}
