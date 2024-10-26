package dev.ak.ecommerce.service;

import dev.ak.ecommerce.entity.Order;
import dev.ak.ecommerce.entity.OrderRequest;
import dev.ak.ecommerce.entity.OrderResponse;
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
                .totalAmount(orderRequest.amount())
                .paymentMethod(orderRequest.paymentMethod())
                .build();
    }

    public OrderResponse fromOrder(Order order) {
        return new OrderResponse(
                order.getId(), order.getReference(), order.getTotalAmount(), order.getPaymentMethod(), order.getCustomerId()
        );
    }
}
