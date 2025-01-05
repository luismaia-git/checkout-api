package com.qikserve.checkout.service.product;


import com.qikserve.checkout.multitenancy.context.TenantContext;
import com.qikserve.checkout.exception.product.notfound.ProductNotFoundException;
import com.qikserve.checkout.model.dto.product.Product;
import com.qikserve.checkout.multitenancy.model.entity.Tenant;
import com.qikserve.checkout.multitenancy.service.TenantService;
import com.qikserve.checkout.service.WiremockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final WiremockService wiremockService;
    private final TenantService tenantService;

    @Override
    public Product getProductById(String productId) {
        String tenantId = TenantContext.getCurrentTenant();
        Tenant tenant = tenantService.findByTenantId(tenantId);
        return wiremockService.findById(productId, tenant.getBaseUrl());
    }

    @Override
    public List<Product> getAllProducts() {
        String tenantId = TenantContext.getCurrentTenant();
        Tenant tenant = tenantService.findByTenantId(tenantId);
        return wiremockService.findAll(tenant.getBaseUrl());
    }

    @Override
    public List<Product> getProductsByIds(List<String> productsIds) {
        String tenantId = TenantContext.getCurrentTenant();
        Tenant tenant = tenantService.findByTenantId(tenantId);
        return wiremockService.findProductsByIds(productsIds, tenant.getBaseUrl());
    }

    @Override
    public void validateProducts(List<String> productsIds) {
        String tenantId = TenantContext.getCurrentTenant();
        Tenant tenant = tenantService.findByTenantId(tenantId);
        List<Product> products = wiremockService.findProductsByIds(productsIds, tenant.getBaseUrl());

        List<String> productsNotFound = productsIds.stream()
                .filter(productId -> products.stream().noneMatch(product -> product.getId().equals(productId))).toList();

        if(!productsNotFound.isEmpty()){
            throw ProductNotFoundException.of(productsNotFound.toString());
        }
    }
}