package dev.ak.ecommerce.product;

import jakarta.validation.constraints.NotNull;

public record PurchaseRequest(
        @NotNull(message = "Product ID is required")
        Integer productId,
        @NotNull(message = "Quantity is required")
        double quantity
) {
}
