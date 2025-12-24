package com.cites.agent.infrastructure.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class TenantFilter implements Filter {

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws java.io.IOException, ServletException {

        var auth = org.springframework.security.core.context.SecurityContextHolder
            .getContext()
            .getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            TenantContext.setTenantId(principal.getTenantId());
        }

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
