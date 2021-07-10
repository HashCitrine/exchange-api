package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
