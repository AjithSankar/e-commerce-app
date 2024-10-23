package dev.ak.ecommerce.service;

import dev.ak.ecommerce.entity.Order;
import dev.ak.ecommerce.entity.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {

    public Order toOrder(OrderRequest orderRequest) {

        if (orderRequest == null) {
            return null;
        }

        return Order.builder()
                .id(orderRequest.id())
                .customerId(orderRequest.customerId())
                .reference(orderRequest.reference())
                .paymentMethod(orderRequest.paymentMethod())
                .build();
    }
}
