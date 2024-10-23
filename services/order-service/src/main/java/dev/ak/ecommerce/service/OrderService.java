package dev.ak.ecommerce.service;

import dev.ak.ecommerce.customer.CustomerClient;
import dev.ak.ecommerce.entity.OrderRequest;
import dev.ak.ecommerce.exception.BusinessException;
import dev.ak.ecommerce.orderline.OrderLineRequest;
import dev.ak.ecommerce.orderline.OrderLineService;
import dev.ak.ecommerce.product.ProductClient;
import dev.ak.ecommerce.product.PurchaseRequest;
import dev.ak.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;

    public Integer createOrder(OrderRequest orderRequest) {
        // check customer
        var customer = this.customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Can't create order, No customer found with id: " + orderRequest.customerId()));
        //purchase the products
        var purchasedProducts = this.productClient.purchaseProducts(orderRequest.products());

        //persist order
        var order = this.orderRepository.save(mapper.toOrder(orderRequest));

        //persist order lines
        for (PurchaseRequest purchaseRequest : orderRequest.products()) {
            orderLineService.saveOrderLine(new OrderLineRequest(null, order.getId(), purchaseRequest.productId(), purchaseRequest.quantity()));
        }

        // start payment process

        //send the order confirmation -> notification-ms(kafka)

        return null;
    }
}
