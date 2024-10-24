package dev.ak.ecommerce.service;

import dev.ak.ecommerce.customer.CustomerClient;
import dev.ak.ecommerce.entity.Order;
import dev.ak.ecommerce.entity.OrderRequest;
import dev.ak.ecommerce.entity.OrderResponse;
import dev.ak.ecommerce.exception.BusinessException;
import dev.ak.ecommerce.kafka.OrderConfirmation;
import dev.ak.ecommerce.kafka.OrderProducer;
import dev.ak.ecommerce.orderline.OrderLineRequest;
import dev.ak.ecommerce.orderline.OrderLineService;
import dev.ak.ecommerce.payment.PaymentClient;
import dev.ak.ecommerce.payment.PaymentRequest;
import dev.ak.ecommerce.product.ProductClient;
import dev.ak.ecommerce.product.PurchaseRequest;
import dev.ak.ecommerce.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

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

        // todo: start payment process
        var paymentRequest = new PaymentRequest(
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        //send the order confirmation -> notification-ms(kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        orderRequest.reference(),
                        orderRequest.amount(),
                        orderRequest.paymentMethod(),
                        customer,
                        purchasedProducts)
        );

        return order.getId();
    }

    public List<OrderResponse> findAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .toList();
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(format("No order found with id:%s", orderId)));
    }
}
