package com.qikserve.checkout.service;

import com.qikserve.checkout.exception.product.notfound.ProductNotFoundException;
import com.qikserve.checkout.exception.tenant.TenantMappersIsWrongException;
import com.qikserve.checkout.exception.tenant.TenantNotFoundException;
import com.qikserve.checkout.exception.wiremock.WiremockUnavailableException;
import com.qikserve.checkout.model.dto.product.Product;
import com.qikserve.checkout.multitenancy.context.TenantContext;
import com.qikserve.checkout.multitenancy.model.dto.TenantProductDTO;
import com.qikserve.checkout.multitenancy.model.entity.Tenant;
import com.qikserve.checkout.multitenancy.service.MappingService;
import com.qikserve.checkout.multitenancy.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WiremockService {

    private final RestTemplate restTemplate;
    private final TenantService tenantService;
    private final MappingService mappingService;

    public Product findById(String productId) {
        String tenantId = TenantContext.getCurrentTenant();

        Tenant tenant = tenantService.findByTenantId(tenantId);
        if(tenant == null) throw TenantNotFoundException.of(tenantId);

        if(tenant.getMappers() == null) throw TenantMappersIsWrongException.of(tenantId);

        String baseUrl = tenant.getBaseUrl();
        String url = baseUrl + "/products/"+productId;
        Map<String, Object> externalProduct;

        try {
            externalProduct = restTemplate.getForObject(url, Map.class);
        } catch (ResourceAccessException exception) {
            throw WiremockUnavailableException.of(url);
        }

        if (externalProduct == null) {
            throw ProductNotFoundException.of(productId);
        }

        return mappingService.mapProductWithPromotions(tenant.getMappers(), externalProduct);
    }

    public List<TenantProductDTO> findProductsByIds(List<String> productsIds) {
        List<TenantProductDTO> products = this.findAll();

        return products.stream()
                .filter(product -> productsIds.contains(product.getId()))
                .toList();
    }

    public List<TenantProductDTO> findAll() {
        String tenantId = TenantContext.getCurrentTenant();

        Tenant tenant = tenantService.findByTenantId(tenantId);
        if(tenant == null) throw TenantNotFoundException.of(tenantId);

        if(tenant.getMappers() == null) throw TenantMappersIsWrongException.of(tenantId);

        String baseUrl = tenant.getBaseUrl();
        String url = baseUrl + "/products";

        List<Map<String, Object>> rawProducts;

        try {
            rawProducts = restTemplate.getForObject(url, List.class);
        } catch (ResourceAccessException exception) {
            throw WiremockUnavailableException.of(url);
        }



        if(rawProducts == null) {
            return List.of();
        }

        return mappingService.mapAllProducts(tenant.getMappers(), rawProducts);
    }
}