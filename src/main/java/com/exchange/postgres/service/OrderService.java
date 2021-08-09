package com.exchange.postgres.service;

import com.exchange.Constants;
import com.exchange.postgres.entity.Order;
import com.exchange.postgres.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {

    private final OrderRepository orderRepository;

    public Long getNextId() {
        return orderRepository.getNextId();
    }

    public void insertOrder(Order order, Long id, Constants.STATUS status) {
        order.setOrderId(id);
        log.info("token : {}", order.toString());

        orderRepository.insertOrder(order, order.getOrderType().toString(), status.toString());
    }

    public void updateOrder(Order order, Long id, Constants.STATUS status) {
        order.setOrderId(id);
        orderRepository.updateOrder(order, status.toString()) ;
    }

    public boolean checkDuplicateId(Long id) {
        boolean result = false;

        // id가 0이 아닌 경우 = 시퀸스를 통해 값을 받아옴 → 중복 체크 필요
        if(id != 0L && orderRepository.checkDuplicateId(id) != 0) {
            // 해당 id로 생성된 행이 없는 경우 = insert
            result = true;
        }

        return result;
    }
}
