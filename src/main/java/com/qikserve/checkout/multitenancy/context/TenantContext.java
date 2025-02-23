package com.qikserve.checkout.multitenancy.context;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TenantContext {

    private TenantContext() {}

    private static final InheritableThreadLocal<String> currentTenant =
            new InheritableThreadLocal<>();

    public static void setCurrentTenant(String tenantId) {
        log.debug("Setting tenantId to " + tenantId);
        currentTenant.set(tenantId);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}
