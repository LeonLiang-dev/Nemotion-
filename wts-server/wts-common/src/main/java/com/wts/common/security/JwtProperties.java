package com.wts.common.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret = "wts-default-secret-key-must-be-at-least-256-bits-long-for-hs256";
    private long accessTokenExpiration = 7200;
    private long refreshTokenExpiration = 604800;
}
