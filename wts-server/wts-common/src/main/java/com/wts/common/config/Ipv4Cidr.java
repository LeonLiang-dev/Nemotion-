package com.wts.common.config;

import java.util.Objects;

/**
 * Minimal IPv4 CIDR matcher. IPv6 is intentionally not accepted because the
 * teacher-machine deployment targets IPv4 LAN prefixes such as 172.18.3.0/24.
 */
public final class Ipv4Cidr {

    private final String value;
    private final long network;
    private final long mask;
    private final int prefixLength;

    private Ipv4Cidr(String value, long network, long mask, int prefixLength) {
        this.value = value;
        this.network = network;
        this.mask = mask;
        this.prefixLength = prefixLength;
    }

    public static Ipv4Cidr parse(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("CIDR must not be blank");
        }
        String trimmed = value.trim();
        String[] parts = trimmed.split("/", -1);
        if (parts.length > 2) {
            throw new IllegalArgumentException("Invalid CIDR: " + value);
        }

        String ip = parts[0].trim();
        int prefix = parts.length == 2 ? parsePrefix(parts[1].trim(), value) : 32;
        long mask = prefix == 0 ? 0 : (0xFFFFFFFFL << (32 - prefix)) & 0xFFFFFFFFL;
        long network = toLong(ip) & mask;
        return new Ipv4Cidr(toDotted(network) + "/" + prefix, network, mask, prefix);
    }

    public static Ipv4Cidr slash24For(String ip) {
        return parse(ip + "/24");
    }

    public boolean contains(String ip) {
        return (toLong(ip) & mask) == network;
    }

    public String value() {
        return value;
    }

    public int prefixLength() {
        return prefixLength;
    }

    public static boolean isIpv4(String ip) {
        try {
            toLong(ip);
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private static int parsePrefix(String value, String original) {
        try {
            int prefix = Integer.parseInt(value);
            if (prefix < 0 || prefix > 32) {
                throw new IllegalArgumentException("Invalid CIDR prefix: " + original);
            }
            return prefix;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid CIDR prefix: " + original, e);
        }
    }

    private static long toLong(String ip) {
        if (ip == null) {
            throw new IllegalArgumentException("IP must not be null");
        }
        String[] parts = ip.trim().split("\\.", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid IPv4 address: " + ip);
        }
        long result = 0;
        for (String part : parts) {
            if (part.isEmpty()) {
                throw new IllegalArgumentException("Invalid IPv4 address: " + ip);
            }
            int octet;
            try {
                octet = Integer.parseInt(part);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid IPv4 address: " + ip, e);
            }
            if (octet < 0 || octet > 255) {
                throw new IllegalArgumentException("Invalid IPv4 address: " + ip);
            }
            result = (result << 8) | octet;
        }
        return result & 0xFFFFFFFFL;
    }

    private static String toDotted(long value) {
        return ((value >> 24) & 0xFF) + "."
                + ((value >> 16) & 0xFF) + "."
                + ((value >> 8) & 0xFF) + "."
                + (value & 0xFF);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Ipv4Cidr that)) {
            return false;
        }
        return network == that.network && mask == that.mask && prefixLength == that.prefixLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(network, mask, prefixLength);
    }
}
