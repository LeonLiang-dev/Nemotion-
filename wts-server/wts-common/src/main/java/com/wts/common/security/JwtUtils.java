package com.wts.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret:wts-default-secret-key-must-be-at-least-256-bits-long-for-hs256}")
    private String secret;

    @Value("${jwt.access-token-expiration:7200}")
    private long accessTokenExpiration; // 秒，默认2小时

    @Value("${jwt.refresh-token-expiration:604800}")
    private long refreshTokenExpiration; // 秒，默认7天

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成访问 Token
     */
    public String generateAccessToken(String userId, String loginName, String name, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("loginName", loginName);
        if (name != null) {
            claims.put("name", name);
        }
        claims.put("userType", userType);
        claims.put("tokenType", "access");
        return createToken(claims, accessTokenExpiration);
    }

    /**
     * 生成刷新 Token
     */
    public String generateRefreshToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("tokenType", "refresh");
        return createToken(claims, refreshTokenExpiration);
    }

    private String createToken(Map<String, Object> claims, long expirationSeconds) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationSeconds * 1000);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 从 Token 中提取 Claims
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 获取用户ID
     */
    public String getUserId(String token) {
        return parseToken(token).get("userId", String.class);
    }

    /**
     * 获取登录名
     */
    public String getLoginName(String token) {
        return parseToken(token).get("loginName", String.class);
    }

    /**
     * 获取用户姓名
     */
    public String getName(String token) {
        return parseToken(token).get("name", String.class);
    }

    /**
     * 获取用户类型
     */
    public String getUserType(String token) {
        return parseToken(token).get("userType", String.class);
    }

    /**
     * 获取 Token 类型
     */
    public String getTokenType(String token) {
        return parseToken(token).get("tokenType", String.class);
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT Token 已过期");
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的 JWT Token");
        } catch (MalformedJwtException e) {
            log.warn("JWT Token 格式错误");
        } catch (SecurityException e) {
            log.warn("JWT 签名错误");
        } catch (IllegalArgumentException e) {
            log.warn("JWT Token 为空");
        }
        return false;
    }

    /**
     * 判断 Token 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Date exp = parseToken(token).getExpiration();
            return exp.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
