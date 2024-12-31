package com.qikserve.checkout.controller;

import com.qikserve.checkout.multitenancy.model.entity.Tenant;
import com.qikserve.checkout.multitenancy.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @GetMapping
    public ResponseEntity<List<Tenant>> getAllTenants() {
        List<Tenant> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody @Valid Tenant tenant) {
        return ResponseEntity.ok(tenantService.createTenant(tenant));
    }

    @PutMapping(value="/{tenantId}")
    public ResponseEntity<Tenant> updateTenant(
            @PathVariable String tenantId,
            @RequestBody Tenant tenant) {
        return ResponseEntity.ok(tenantService.updateTenant(tenantId, tenant));
    }

    @DeleteMapping(value="/{tenantId}")
    public ResponseEntity<Void> deleteTenant(@PathVariable String tenantId) {
        tenantService.deleteTenant(tenantId);
        return ResponseEntity.noContent().build();
    }
}