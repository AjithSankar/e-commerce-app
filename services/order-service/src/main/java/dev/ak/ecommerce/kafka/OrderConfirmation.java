package dev.ak.ecommerce.kafka;

import dev.ak.ecommerce.customer.CustomerResponse;
import dev.ak.ecommerce.entity.PaymentMethod;
import dev.ak.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customerResponse,
        List<PurchaseResponse> products
) {
}
