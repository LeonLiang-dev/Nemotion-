package com.wts.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wts.common.result.R;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * Restricts teacher-machine deployments to local loopback and allowed LAN CIDRs.
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class LanAccessFilter extends OncePerRequestFilter {

    private static final long AUTO_CIDR_CACHE_MILLIS = Duration.ofMinutes(1).toMillis();

    private final LanAccessProperties properties;
    private final ObjectMapper objectMapper;
    private final Supplier<List<Ipv4Cidr>> autoCidrSupplier;

    private volatile List<Ipv4Cidr> manualCidrs = List.of();
    private volatile List<Ipv4Cidr> cachedAutoCidrs = List.of();
    private volatile long cachedAutoCidrsAt = 0L;

    @Autowired
    public LanAccessFilter(LanAccessProperties properties, ObjectMapper objectMapper) {
        this(properties, objectMapper, LocalSubnetDetector::detectAutoCidrs);
    }

    LanAccessFilter(LanAccessProperties properties,
                    ObjectMapper objectMapper,
                    Supplier<List<Ipv4Cidr>> autoCidrSupplier) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.autoCidrSupplier = autoCidrSupplier;
    }

    @PostConstruct
    void refreshManualCidrs() {
        List<Ipv4Cidr> parsed = new ArrayList<>();
        for (String cidr : properties.getAllowedCidrs()) {
            if (cidr == null || cidr.trim().isEmpty()) {
                continue;
            }
            parsed.add(Ipv4Cidr.parse(cidr));
        }
        manualCidrs = List.copyOf(parsed);
        log.info("局域网访问控制: enabled={}, autoSameSubnet={}, manualCidrs={}",
                properties.isEnabled(), properties.isAutoSameSubnet(), manualCidrs);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!properties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String remoteAddress = normalizeRemoteAddress(request.getRemoteAddr());
        if (isLoopback(remoteAddress) || isAllowed(remoteAddress)) {
            filterChain.doFilter(request, response);
            return;
        }

        log.warn("拒绝非允许局域网请求: remoteAddr={}, method={}, uri={}",
                remoteAddress, request.getMethod(), request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), R.fail(403, "当前设备不在允许的局域网范围内"));
    }

    private boolean isAllowed(String remoteAddress) {
        if (!Ipv4Cidr.isIpv4(remoteAddress)) {
            return false;
        }

        for (Ipv4Cidr cidr : allowedCidrs()) {
            if (cidr.contains(remoteAddress)) {
                return true;
            }
        }
        return false;
    }

    private List<Ipv4Cidr> allowedCidrs() {
        List<Ipv4Cidr> allowed = new ArrayList<>();
        if (properties.isAutoSameSubnet()) {
            allowed.addAll(autoCidrs());
        }
        allowed.addAll(manualCidrs);
        return allowed;
    }

    private List<Ipv4Cidr> autoCidrs() {
        long now = System.currentTimeMillis();
        if (now - cachedAutoCidrsAt > AUTO_CIDR_CACHE_MILLIS) {
            cachedAutoCidrs = List.copyOf(autoCidrSupplier.get());
            cachedAutoCidrsAt = now;
        }
        return cachedAutoCidrs;
    }

    private static String normalizeRemoteAddress(String remoteAddress) {
        if (remoteAddress == null) {
            return "";
        }
        String normalized = remoteAddress.trim();
        String lower = normalized.toLowerCase(Locale.ROOT);
        if (lower.startsWith("::ffff:")) {
            return normalized.substring(7);
        }
        return normalized;
    }

    private static boolean isLoopback(String remoteAddress) {
        return "127.0.0.1".equals(remoteAddress)
                || "0:0:0:0:0:0:0:1".equals(remoteAddress)
                || "::1".equals(remoteAddress);
    }
}
