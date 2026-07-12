package com.ice.config;

import com.ice.auth.AdminAuthInterceptor;
import com.ice.auth.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AdminAuthInterceptor adminAuthInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor, AdminAuthInterceptor adminAuthInterceptor) {
        this.authInterceptor = authInterceptor;
        this.adminAuthInterceptor = adminAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns(
                        "/api/v1/auth/**",
                        "/api/v1/articles/featured",
                        "/api/v1/categories/**",
                        "/api/v1/config/**",
                        "/api/v1/rankings/**",
                        "/api/v1/topics/ongoing",
                        "/api/v1/topics/*",
                        "/api/v1/topics/*/articles",
                        "/api/v1/search",
                        "/api/v1/uploads/files/**"
                );
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/api/v1/admin/**");
    }
}
