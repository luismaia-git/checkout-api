package com.qikserve.checkout.multitenancy.repository;

import java.util.Optional;

import com.qikserve.checkout.multitenancy.model.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TenantRepository extends JpaRepository<Tenant, String> {

    @Query("select t from tenants t where t.id = :tenantId")
    Optional<Tenant> findByTenantId(@Param("tenantId") String tenantId);
}
