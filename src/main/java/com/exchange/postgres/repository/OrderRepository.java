package com.exchange.postgres.repository;

import com.exchange.Constants;
import com.exchange.postgres.entity.BankStatement;
import com.exchange.postgres.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT NEXTVAL('order_order_id_seq')", nativeQuery = true)
    Long getNextId();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO \"order\" (order_id, order_date, order_member, currency, order_type, price, quantity, stock, status, upt_date) " +
            "VALUES (:#{#order.orderId}, Now(), '', :#{#order.currency}, :#{#orderType}, :#{#order.price}, :#{#order.quantity}, :#{#order.quantity}, :#{#status}, Now())", nativeQuery = true)
    void insertOrder(@Param("order") Order order, @Param("orderType") String orderType, @Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE \"order\" SET status = :#{#status}, upt_date = Now() WHERE order_id = :#{#order.orderId}", nativeQuery = true)
    void updateOrder(@Param("order") Order order, @Param("status") String status);

    @Query(value = "SELECT count(*) FROM \"order\" WHERE order_id = ?1", nativeQuery = true)
    int checkDuplicateId(Long id);
}
