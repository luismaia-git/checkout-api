package com.qikserve.checkout.multitenancy.service;

import com.qikserve.checkout.multitenancy.model.entity.Tenant;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TenantService {
    Tenant findByTenantId(@Param("tenantId") String tenantId);
    List<Tenant> getAllTenants();
    Tenant createTenant(Tenant tenant);
    Tenant updateTenant(String tenantId, Tenant tenant);
    void deleteTenant(String tenantId);
}
