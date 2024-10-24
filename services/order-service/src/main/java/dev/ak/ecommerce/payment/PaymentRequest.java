package dev.ak.ecommerce.payment;


import dev.ak.ecommerce.customer.CustomerResponse;
import dev.ak.ecommerce.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Integer orderId,
    String orderReference,
    CustomerResponse customer
) {
}
