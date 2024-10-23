package dev.ak.ecommerce.exception;

public class ProductPurchaseException extends RuntimeException {

    public ProductPurchaseException(String message) {
        super(message);
    }

    public ProductPurchaseException(String message, Throwable t) {
        super(message, t);
    }

}
