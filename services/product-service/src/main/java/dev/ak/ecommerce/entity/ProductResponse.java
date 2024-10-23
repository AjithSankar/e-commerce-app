package dev.ak.ecommerce.entity;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,
        String name,
        String description,
        double quantity,
        BigDecimal price,
        Integer categoryId,
        String categoryName,
        String categoryDescription
) {
}
