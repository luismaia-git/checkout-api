package com.qikserve.checkout.service.product;

import com.qikserve.checkout.model.dto.product.Product;

import java.util.List;

public interface IProductService {
    Product getProductById (String productId);
    List<Product> getAllProducts();
    List<Product> getProductsByIds(List<String> productsIds);
    void validateProducts(List<String> productsIds);
}
