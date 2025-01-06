package com.qikserve.checkout.controller;

import com.qikserve.checkout.model.dto.product.Product;
import com.qikserve.checkout.multitenancy.context.TenantContext;
import com.qikserve.checkout.multitenancy.model.dto.TenantMappersDTO;
import com.qikserve.checkout.multitenancy.model.dto.TenantProductDTO;
import com.qikserve.checkout.multitenancy.model.entity.Tenant;
import com.qikserve.checkout.multitenancy.service.TenantService;
import com.qikserve.checkout.service.WiremockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;
    private final WiremockService wiremockService;

    @GetMapping
    public ResponseEntity<List<Tenant>> getAllTenants() {
        List<Tenant> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    @GetMapping(value="/{tenantId}/products")
    public ResponseEntity<List<TenantProductDTO>> getProductsByTenant(
            @PathVariable String tenantId) {
        TenantContext.setCurrentTenant(tenantId);
        return ResponseEntity.ok(wiremockService.findAll());
    }

    @GetMapping(value="/{tenantId}/products/{productId}")
    public ResponseEntity<Product> getProductByIdByTenant(
            @PathVariable String tenantId,  @PathVariable String productId) {
        TenantContext.setCurrentTenant(tenantId);
        return ResponseEntity.ok(wiremockService.findById(productId));
    }

    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
        return ResponseEntity.ok(tenantService.createTenant(tenant));
    }

    @PutMapping(value="/{tenantId}")
    public ResponseEntity<Tenant> updateTenant(
            @PathVariable String tenantId,
            @RequestBody Tenant tenant) {

        return ResponseEntity.ok(tenantService.updateTenant(tenantId, tenant));
    }

    @PutMapping(value="/{tenantId}/mappers")
    public ResponseEntity<Tenant> updateTenantMappers(
            @PathVariable String tenantId,
            @RequestBody TenantMappersDTO tenantMappers) {
        return ResponseEntity.ok(tenantService.updateTenantMappers(tenantId, tenantMappers.getMappers()));
    }

    @DeleteMapping(value="/{tenantId}")
    public ResponseEntity<Void> deleteTenant(@PathVariable String tenantId) {
        tenantService.deleteTenant(tenantId);
        return ResponseEntity.noContent().build();
    }
}