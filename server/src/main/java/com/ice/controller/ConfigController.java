package com.ice.controller;

import com.ice.dto.config.ClientConfigResponse;
import com.ice.service.ConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/client")
    public ClientConfigResponse client() {
        return configService.getClientConfig();
    }
}
