package com.qikserve.checkout.service.product;

import com.qikserve.checkout.model.dto.product.Product;
import com.qikserve.checkout.multitenancy.model.dto.TenantProductDTO;

import java.util.List;

public interface IProductService {
    Product getProductById (String productId);
    List<TenantProductDTO> getAllProducts();
    List<TenantProductDTO> getProductsByIds(List<String> productsIds);
    void validateProducts(List<String> productsIds);
}
