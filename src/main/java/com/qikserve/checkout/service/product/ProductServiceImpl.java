package com.qikserve.checkout.service.product;

import com.qikserve.checkout.exception.product.notfound.ProductNotFoundException;
import com.qikserve.checkout.model.dto.product.Product;
import com.qikserve.checkout.multitenancy.model.dto.TenantProductDTO;
import com.qikserve.checkout.service.WiremockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final WiremockService wiremockService;

    @Override
    public Product getProductById(String productId) {

        return wiremockService.findById(productId);
    }

    @Override
    public List<TenantProductDTO> getAllProducts() {
        return wiremockService.findAll();
    }

    @Override
    public List<TenantProductDTO> getProductsByIds(List<String> productsIds) {
        return wiremockService.findProductsByIds(productsIds);
    }

    @Override
    public void validateProducts(List<String> productsIds) {
        List<TenantProductDTO> products = wiremockService.findProductsByIds(productsIds);
        List<String> productsNotFound = productsIds.stream()
                .filter(productId -> products.stream().noneMatch(product -> product.getId().equals(productId))).toList();

        if(!productsNotFound.isEmpty()){
            throw ProductNotFoundException.of(productsNotFound.toString());
        }
    }
}