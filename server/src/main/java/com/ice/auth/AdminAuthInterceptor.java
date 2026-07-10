package com.ice.auth;

import com.ice.domain.UserRole;
import com.ice.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserRole role = AuthContext.requireRole();
        if (!role.canAccessAdmin()) {
            throw new ApiException("FORBIDDEN", "非管理员账号", HttpStatus.FORBIDDEN);
        }
        return true;
    }
}
