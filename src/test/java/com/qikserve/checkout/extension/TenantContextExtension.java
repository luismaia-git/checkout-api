package com.qikserve.checkout.extension;

import com.qikserve.checkout.multitenancy.context.TenantContext;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockedStatic;
import org.mockito.Mockito;


public class TenantContextExtension implements BeforeEachCallback, AfterEachCallback {
    private MockedStatic<TenantContext> mockedTenantContext;
    private String tenantId = "tenant1";

    private void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    private String getTenantId() {
        return tenantId;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        mockedTenantContext = Mockito.mockStatic(TenantContext.class);
        mockedTenantContext.when(TenantContext::getCurrentTenant).thenReturn(tenantId);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (mockedTenantContext != null) {
            mockedTenantContext.close();
        }
    }
}