package dev.ak.ecommerce.service;

import dev.ak.ecommerce.entity.Customer;
import dev.ak.ecommerce.entity.CustomerRequest;
import dev.ak.ecommerce.entity.CustomerResponse;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest customerRequest) {

        if (customerRequest == null) {
            return null;
        }

        return Customer.builder()
                .firstName(customerRequest.firstName())
                .lastName(customerRequest.lastName())
                .email(customerRequest.email())
                .address(customerRequest.address())
                .build();
    }

    public CustomerResponse toCustomerResponse(Customer customer) {

            if (customer == null) {
                return null;
            }

            return new CustomerResponse(
                    customer.getId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail(),
                    customer.getAddress()
            );
    }
}
