package com.ice.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ice.config.AuthProperties;
import com.ice.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WechatAuthClient {

    private final AuthProperties authProperties;
    private final RestClient restClient;

    public WechatAuthClient(AuthProperties authProperties) {
        this.authProperties = authProperties;
        this.restClient = RestClient.create();
    }

    public String codeToOpenid(String code) {
        if (code == null || code.isBlank()) {
            throw new ApiException("WX_LOGIN_FAILED", "code 无效", HttpStatus.BAD_REQUEST);
        }
        if (code.startsWith("dev:")) {
            return code.substring("dev:".length()).trim();
        }
        String appId = authProperties.getWxAppId();
        String appSecret = authProperties.getWxAppSecret();
        if (appId == null || appId.isBlank() || appSecret == null || appSecret.isBlank()) {
            throw new ApiException(
                    "WX_LOGIN_FAILED",
                    "微信登录未配置，本地请使用 dev:your-openid 作为 code",
                    HttpStatus.BAD_REQUEST
            );
        }
        Code2SessionResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.weixin.qq.com")
                        .path("/sns/jscode2session")
                        .queryParam("appid", appId)
                        .queryParam("secret", appSecret)
                        .queryParam("js_code", code)
                        .queryParam("grant_type", "authorization_code")
                        .build())
                .retrieve()
                .body(Code2SessionResponse.class);
        if (response == null || response.openid == null || response.openid.isBlank()) {
            String err = response == null ? "empty response" : response.errmsg;
            throw new ApiException("WX_LOGIN_FAILED", "微信登录失败: " + err, HttpStatus.BAD_REQUEST);
        }
        return response.openid;
    }

    private static class Code2SessionResponse {
        @JsonProperty("openid")
        private String openid;
        @JsonProperty("errcode")
        private Integer errcode;
        @JsonProperty("errmsg")
        private String errmsg;
    }
}
