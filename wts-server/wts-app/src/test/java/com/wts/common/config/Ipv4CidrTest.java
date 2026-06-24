package com.wts.common.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Ipv4CidrTest {

    @Test
    void slash24MatchesSamePrefixOnly() {
        Ipv4Cidr cidr = Ipv4Cidr.parse("172.18.3.0/24");

        assertTrue(cidr.contains("172.18.3.45"));
        assertTrue(cidr.contains("172.18.3.254"));
        assertFalse(cidr.contains("172.18.4.45"));
    }

    @Test
    void exactIpDefaultsToSlash32() {
        Ipv4Cidr cidr = Ipv4Cidr.parse("192.168.1.10");

        assertEquals(32, cidr.prefixLength());
        assertTrue(cidr.contains("192.168.1.10"));
        assertFalse(cidr.contains("192.168.1.11"));
    }

    @Test
    void invalidCidrIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Ipv4Cidr.parse("172.18.3.0/33"));
        assertThrows(IllegalArgumentException.class, () -> Ipv4Cidr.parse("172.18.3.999/24"));
    }
}
