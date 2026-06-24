package com.wts.common.security;

import org.springframework.util.StringUtils;

/**
 * Authenticated user details used by application modules.
 */
public record CurrentUser(String id, String loginName, String name, String userType) {

    public String displayName() {
        if (StringUtils.hasText(name)) {
            return name;
        }
        return StringUtils.hasText(loginName) ? loginName : id;
    }

    public boolean isAdmin() {
        return "1".equals(userType) || "3".equals(userType);
    }
}
