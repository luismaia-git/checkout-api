package com.qikserve.checkout.multitenancy.service;

import com.qikserve.checkout.exception.tenant.TenantNotFoundException;
import com.qikserve.checkout.multitenancy.model.dto.TenantUpdateDTO;
import com.qikserve.checkout.multitenancy.model.entity.Tenant;
import com.qikserve.checkout.multitenancy.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    @Override
    public Tenant findByTenantId(String tenantId) {
        return tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> TenantNotFoundException.of(tenantId));
    }

    @Override
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    @Override
    public Tenant createTenant(Tenant tenant) {

        Tenant tenantBuilt = Tenant.builder()
                .baseUrl(tenant.getBaseUrl())
                .name(tenant.getName())
                .mappers(tenant.getMappers())
                .build();

        return tenantRepository.save(tenantBuilt);
    }

    @Override
    public Tenant updateTenant(String tenantId, TenantUpdateDTO tenant) {
        Tenant tenantFound = this.findByTenantId(tenantId);
        tenantFound.setName(tenant.getName());
        tenantFound.setBaseUrl(tenant.getBaseUrl());
        tenantFound.setMappers(tenant.getMappers());
        return tenantRepository.save(tenantFound);
    }

    public Tenant updateTenantMappers(String tenantId, String mappers) {
        Tenant tenantFound = this.findByTenantId(tenantId);
        tenantFound.setMappers(mappers);
        return tenantRepository.save(tenantFound);
    }

    @Override
    @Transactional
    public void deleteTenant(String tenantId) {
        Tenant tenant = this.findByTenantId(tenantId);
        tenantRepository.delete(tenant);
    }
}