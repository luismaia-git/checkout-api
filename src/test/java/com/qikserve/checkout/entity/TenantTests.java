package com.qikserve.checkout.entity;

import com.qikserve.checkout.factory.TenantFactory;
import com.qikserve.checkout.multitenancy.repository.TenantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TenantTests {
    @Autowired
    private TenantRepository tenantRepository;

    @Test
    public void shouldCreateTenantSuccessfully() {
        var tenant = TenantFactory.makeTenant();
        var tenantCreated = tenantRepository.save(tenant);
        var tenants = tenantRepository.findAll();

        assertEquals(1, tenants.size());
        assertNotNull(tenantCreated);
        assertEquals(tenant, tenantCreated);
        assertEquals(tenants.getFirst(), tenantCreated);

    }

    @Test
    public void shouldGetTenantByIdSuccessfully() {
        var tenant = TenantFactory.makeTenant();
        var tenantCreated = tenantRepository.save(tenant);
        var tenantFound = tenantRepository.findById(tenantCreated.getId());

        assertTrue(tenantFound.isPresent());
        assertEquals(tenantCreated, tenantFound.get());
    }

    @Test
    public void shouldUpdateTenantSuccessfully() {
        var tenant = TenantFactory.makeTenant();
        var tenantCreated = tenantRepository.save(tenant);
        var tenantFound = tenantRepository.findById(tenantCreated.getId());
        var tenants = tenantRepository.findAll();

        assertEquals(1, tenants.size());
        assertTrue(tenantFound.isPresent());
        assertEquals(tenantCreated, tenantFound.get());

        tenantCreated.setBaseUrl("http://localhost:8080");
        var tenantUpdated = tenantRepository.save(tenantCreated);
        var tenantFoundUpdated = tenantRepository.findById(tenantUpdated.getId());

        assertTrue(tenantFoundUpdated.isPresent());
        assertEquals(tenantUpdated, tenantFoundUpdated.get());
    }

    @Test
    public void shouldDeleteTenantSuccessfully() {
        var tenant = TenantFactory.makeTenant();
        var tenantCreated = tenantRepository.save(tenant);
        var tenantFound = tenantRepository.findById(tenantCreated.getId());

        assertTrue(tenantFound.isPresent());
        assertEquals(tenantCreated, tenantFound.get());

        tenantRepository.delete(tenantCreated);
        var tenantFoundDeleted = tenantRepository.findById(tenantCreated.getId());

        assertTrue(tenantFoundDeleted.isEmpty());
    }

}
