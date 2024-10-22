package dev.ak.ecommerce.service;

import dev.ak.ecommerce.entity.Customer;
import dev.ak.ecommerce.entity.CustomerRequest;
import dev.ak.ecommerce.entity.CustomerResponse;
import dev.ak.ecommerce.exception.CustomerNotFoundException;
import dev.ak.ecommerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public String createCustomer(CustomerRequest customerRequest) {
        var customer = customerRepository.save(customerMapper.toCustomer(customerRequest));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest customerRequest) {
        var customer = customerRepository.findById(customerRequest.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Customer with id %s not found", customerRequest.id())
                ));

        mergeCustomer(customer, customerRequest);
        customerRepository.save(customer);
    }

    public List<CustomerResponse> findAllCustomers() {

        return customerRepository.findAll().stream()
                .map(customerMapper::toCustomerResponse)
                .toList();
    }

    public Boolean existsById(String customerId) {
        return customerRepository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(String customerId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Customer with id %s not found", customerId)
                ));

        return customerMapper.toCustomerResponse(customer);
    }

    public void deleteById(String customerId) {
        customerRepository.deleteById(customerId);
    }

    private void mergeCustomer(Customer customer, CustomerRequest customerRequest) {

        if (StringUtils.isNotBlank(customerRequest.firstName())) {
            customer.setFirstName(customerRequest.firstName());
        }

        if (StringUtils.isNotBlank(customerRequest.lastName())) {
            customer.setLastName(customerRequest.lastName());
        }

        if (StringUtils.isNotBlank(customerRequest.email())) {
            customer.setEmail(customerRequest.email());
        }

        if (customerRequest.address() != null) {
            customer.setAddress(customerRequest.address());
        }
    }

}
