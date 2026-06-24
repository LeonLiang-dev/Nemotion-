package com.wts.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LanAccessFilterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void allowsLoopbackAccess() throws Exception {
        LanAccessFilter filter = filter(true, true, List.of(), List.of(Ipv4Cidr.parse("172.18.3.0/24")));

        MockHttpServletResponse response = perform(filter, "127.0.0.1");

        assertEquals(200, response.getStatus());
    }

    @Test
    void allowsAutoSameSubnetAccess() throws Exception {
        LanAccessFilter filter = filter(true, true, List.of(), List.of(Ipv4Cidr.parse("172.18.3.0/24")));

        MockHttpServletResponse response = perform(filter, "172.18.3.99");

        assertEquals(200, response.getStatus());
    }

    @Test
    void rejectsDifferentSubnetAccess() throws Exception {
        LanAccessFilter filter = filter(true, true, List.of(), List.of(Ipv4Cidr.parse("172.18.3.0/24")));

        MockHttpServletResponse response = perform(filter, "172.18.4.20");

        assertEquals(403, response.getStatus());
        assertTrue(response.getContentAsString().contains("当前设备不在允许的局域网范围内"));
    }

    @Test
    void manualCidrsCanReplaceAutoSubnets() throws Exception {
        LanAccessFilter filter = filter(true, false, List.of("10.8.0.0/16"), List.of(Ipv4Cidr.parse("172.18.3.0/24")));

        assertEquals(200, perform(filter, "10.8.7.21").getStatus());
        assertEquals(403, perform(filter, "172.18.3.21").getStatus());
    }

    @Test
    void disabledFilterAllowsAllAccess() throws Exception {
        LanAccessFilter filter = filter(false, true, List.of(), List.of(Ipv4Cidr.parse("172.18.3.0/24")));

        MockHttpServletResponse response = perform(filter, "203.0.113.1");

        assertEquals(200, response.getStatus());
    }

    private LanAccessFilter filter(boolean enabled,
                                   boolean autoSameSubnet,
                                   List<String> manualCidrs,
                                   List<Ipv4Cidr> autoCidrs) {
        LanAccessProperties properties = new LanAccessProperties();
        properties.setEnabled(enabled);
        properties.setAutoSameSubnet(autoSameSubnet);
        properties.setAllowedCidrs(manualCidrs);
        LanAccessFilter filter = new LanAccessFilter(properties, objectMapper, () -> autoCidrs);
        filter.refreshManualCidrs();
        return filter;
    }

    private MockHttpServletResponse perform(LanAccessFilter filter, String remoteAddr)
            throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/health");
        request.setRemoteAddr(remoteAddr);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);
        return response;
    }
}
