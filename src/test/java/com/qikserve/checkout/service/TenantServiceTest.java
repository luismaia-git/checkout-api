package com.qikserve.checkout.service;


import com.qikserve.checkout.exception.tenant.TenantNotFoundException;
import com.qikserve.checkout.factory.TenantFactory;
import com.qikserve.checkout.multitenancy.model.entity.Tenant;
import com.qikserve.checkout.multitenancy.repository.TenantRepository;
import com.qikserve.checkout.multitenancy.service.TenantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TenantServiceTest {

    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private TenantServiceImpl tenantService;

    @Test
    public void testGetTenantById_WhenTenantExists_ShouldReturnTenant() {
        // Given
        var tenantId = UUID.randomUUID().toString();
        var tenant = Tenant.builder().id(tenantId).name("TenantName").mappers("").build();
        when(tenantRepository.findByTenantId(tenantId)).thenReturn(Optional.of(tenant));

        // When
        var result = tenantService.findByTenantId(tenantId);

        // Then
        assertNotNull(result);
        assertEquals(tenantId, result.getId());
    }

    @Test
    public void testGetTenantById_WhenTenantDoesNotExist_ShouldReturnEmpty() {
        // Given
        var tenantId = UUID.randomUUID().toString();
        when(tenantRepository.findByTenantId(tenantId)).thenReturn(Optional.empty());

        assertThrows(TenantNotFoundException.class, () -> tenantService.findByTenantId(tenantId));
    }

    @Test
    public void testCreateTenant_ShouldReturnNewTenant() {
        // Given
        var id = UUID.randomUUID().toString();
        var tenant = Tenant.builder().name("TenantName").mappers("").build();
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(i -> i.getArgument(0, Tenant.class).withId(id));

        // When
        var result = tenantService.createTenant(tenant);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("TenantName", result.getName());
    }

    @Test
    public void testDeleteTenant_WhenTenantExists_ShouldDeleteTenant() {
        // Given
        var tenantId = UUID.randomUUID().toString();
        var tenant = TenantFactory.makeTenant().withId(tenantId);
        when(tenantRepository.findByTenantId(tenantId)).thenReturn(Optional.of(tenant));

        // When
        tenantService.deleteTenant(tenantId);

        // Then
        verify(tenantRepository, times(1)).delete(tenant);
    }

    @Test
    public void testDeleteTenant_WhenTenantDoesNotExist_ShouldThrowException() {
        // Given
        var tenantId = UUID.randomUUID().toString();
        when(tenantRepository.findByTenantId(tenantId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> tenantService.deleteTenant(tenantId));
    }
}
