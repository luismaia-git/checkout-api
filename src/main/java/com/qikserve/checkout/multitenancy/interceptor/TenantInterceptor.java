package com.qikserve.checkout.multitenancy.interceptor;

import com.qikserve.checkout.multitenancy.context.TenantContext;
import com.qikserve.checkout.exception.tenant.TenantIsMissingInHeader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Component
public class TenantInterceptor implements HandlerInterceptor {

    public static final String X_TENANT_ID = "X-TENANT-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String tenantId = request.getHeader(X_TENANT_ID);
        if (tenantId == null || tenantId.isEmpty()) {
            log.info("TenantId is missing in header");
            throw TenantIsMissingInHeader.of(tenantId);
        }
        TenantContext.setCurrentTenant(tenantId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        TenantContext.clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //do nothing
    }

}