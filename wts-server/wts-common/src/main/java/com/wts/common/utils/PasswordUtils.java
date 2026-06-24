package com.wts.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码工具 - 兼容旧系统 MD5 和新系统 BCrypt
 * 旧系统密码算法: MD5(password + loginname) 转大写
 */
public class PasswordUtils {

    /**
     * 计算旧系统密码哈希: MD5(password + loginname) 转大写
     */
    public static String md5Password(String password, String loginName) {
        return md5(password + loginName).toUpperCase();
    }

    /**
     * 判断是否为 BCrypt 密码 (以 $2a$/$2b$/$2y$ 开头)
     */
    public static boolean isBCrypt(String password) {
        return password != null && password.matches("^\\$2[aby]\\$.+$");
    }

    /**
     * 判断是否为 32 位 MD5 密码
     */
    public static boolean isMd5(String password) {
        return password != null && password.matches("^[A-Fa-f0-9]{32}$");
    }

    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not found", e);
        }
    }
}
