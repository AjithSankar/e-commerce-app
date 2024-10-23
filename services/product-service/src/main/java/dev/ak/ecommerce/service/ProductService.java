package dev.ak.ecommerce.service;

import dev.ak.ecommerce.entity.ProductPurchaseRequest;
import dev.ak.ecommerce.entity.ProductPurchaseResponse;
import dev.ak.ecommerce.entity.ProductRequest;
import dev.ak.ecommerce.entity.ProductResponse;
import dev.ak.ecommerce.exception.ProductPurchaseException;
import dev.ak.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(ProductRequest productRequest) {
        var product = productMapper.toProduct(productRequest);
        return productRepository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> productPurchaseRequest) {

        var productIds = productPurchaseRequest.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        var storedProducts = productRepository.findByIdInOrderById(productIds);

        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exists");
        }

        var storedRequest = productPurchaseRequest.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        for (int i = 0; i < storedProducts.size(); i++) {
            var productFromDb = storedProducts.get(i);
            var productRequest = storedRequest.get(i);

            if (productFromDb.getQuantity() < productRequest.quantity()) {
                log.info("Insufficient stored quantity for the product with id: {} " , productFromDb.getId());
                throw new ProductPurchaseException("Insufficient stored quantity for the product with id: " + productFromDb.getId());
            }

            var availableQuantity = productFromDb.getQuantity() - productRequest.quantity();
            productFromDb.setQuantity(availableQuantity);
            productRepository.save(productFromDb);
            log.info("Product with id {} updated with availableQuantity {} in DB", productFromDb.getId(), availableQuantity);
            purchasedProducts.add(productMapper.toProductPurchaseResponse(productFromDb, productRequest.quantity()));

        }

        return purchasedProducts;
    }

    public ProductResponse findById(Integer id) {
        return productRepository
                .findById(id)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product with Id " + id + " not found"));

    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
