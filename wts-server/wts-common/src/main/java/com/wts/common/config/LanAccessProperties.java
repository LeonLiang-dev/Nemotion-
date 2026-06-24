package com.wts.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * LAN access control for teacher-machine deployments.
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.access.lan")
public class LanAccessProperties {

    /**
     * Enable request IP filtering.
     */
    private boolean enabled = false;

    /**
     * Automatically allow /24 subnets for the teacher machine's local IPv4 addresses.
     */
    private boolean autoSameSubnet = true;

    /**
     * Additional or replacement IPv4 CIDR ranges. Use LAN_AUTO_SAME_SUBNET=false
     * when these should fully replace automatic ranges.
     */
    private List<String> allowedCidrs = new ArrayList<>();
}
