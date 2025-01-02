package com.rahul.voucher_api.service;

import com.rahul.voucher_api.entity.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(UUID id);
    List<Product> getAllProducts();
    Product getProductById(UUID id);
}