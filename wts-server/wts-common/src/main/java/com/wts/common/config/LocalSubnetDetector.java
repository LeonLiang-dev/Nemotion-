package com.wts.common.config;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
final class LocalSubnetDetector {

    private LocalSubnetDetector() {
    }

    static List<Ipv4Cidr> detectAutoCidrs() {
        Set<Ipv4Cidr> cidrs = new LinkedHashSet<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces == null) {
                return List.of();
            }
            for (NetworkInterface networkInterface : Collections.list(interfaces)) {
                if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }
                List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress address : addresses) {
                    if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                        cidrs.add(Ipv4Cidr.slash24For(address.getHostAddress()));
                    }
                }
            }
        } catch (SocketException e) {
            log.warn("无法检测本机局域网网段，将仅使用手动配置的允许网段", e);
        }
        return new ArrayList<>(cidrs);
    }
}
