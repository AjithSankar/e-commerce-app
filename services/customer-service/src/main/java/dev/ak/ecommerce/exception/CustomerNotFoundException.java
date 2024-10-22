package dev.ak.ecommerce.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String customerNotFound) {
        super(customerNotFound);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
