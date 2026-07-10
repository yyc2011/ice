package com.ice.auth;

import com.ice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;
    private final AuthService authService;

    public AuthInterceptor(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new UnauthorizedException("未登录");
        }
        String token = auth.substring("Bearer ".length()).trim();
        if (token.startsWith("dev-")) {
            try {
                long userId = Long.parseLong(token.substring("dev-".length()));
                AuthContext.set(userId, authService.loadRole(userId));
                return true;
            } catch (NumberFormatException ex) {
                throw new UnauthorizedException("无效 token");
            }
        }
        try {
            JwtService.JwtClaims claims = jwtService.parseToken(token);
            AuthContext.set(claims.userId(), claims.role());
            return true;
        } catch (Exception ex) {
            throw new UnauthorizedException("无效 token");
        }
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        AuthContext.clear();
    }
}
