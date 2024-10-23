package dev.ak.ecommerce.entity;

import dev.ak.ecommerce.product.PurchaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Integer id,

        String reference,

        @Positive(message = "Amount should be greater than zero")
        BigDecimal amount,
        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        @NotNull(message = "Customer ID is required")
        @NotEmpty(message = "Customer ID should not be empty")
        @NotBlank(message = "Customer ID should not be blank")
        String customerId,

        @NotNull(message = "you should provide at least one product")
        List<PurchaseRequest> products
) {
}
