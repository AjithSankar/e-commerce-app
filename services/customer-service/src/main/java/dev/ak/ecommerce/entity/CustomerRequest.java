package dev.ak.ecommerce.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
         String id,

         @NotNull(message = "Customer firstName is required")
         String firstName,

         @NotNull(message = "Customer lastName is required")
         String lastName,

         @NotNull(message = "Customer email is required")
         @Email(message = "Customer email is invalid")
         String email,

         Address address
) {
}
