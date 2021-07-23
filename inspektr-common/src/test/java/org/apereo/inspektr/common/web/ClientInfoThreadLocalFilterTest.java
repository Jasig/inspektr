package org.apereo.inspektr.common.web;

import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClientInfoThreadLocalFilterTest {
    private static final String MOCK_CLIENT_IP = "114.514.19.19";
    private static final String MOCK_USER_AGENT = "inspektr-mock/v0.0.0";

    @Test
    public void testDefaultClientInfoCollector() throws Exception {
        MockFilterConfig filterConfig = new MockFilterConfig();
        ClientInfoThreadLocalFilter filter = new ClientInfoThreadLocalFilter();
        filter.init(filterConfig);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(MOCK_CLIENT_IP);
        request.addHeader("User-Agent", MOCK_USER_AGENT);

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain filterChain = (servletRequest, servletResponse) -> {
            ClientInfo clientInfo = ClientInfoHolder.getClientInfo();
            assertNotNull("client info must not be null after ClientInfoThreadLocalFilter proceed", clientInfo);
            assertEquals(MOCK_CLIENT_IP, clientInfo.getClientIpAddress());
            assertEquals(MOCK_USER_AGENT, clientInfo.getUserAgent());
        };

        filter.doFilter(request, response, filterChain);
    }

    @Test
    public void testCustomClientInfoCollector() throws Exception {
        MockFilterConfig filterConfig = new MockFilterConfig();
        filterConfig.addInitParameter(ClientInfoThreadLocalFilter.CONST_CLIENT_INFO_COLLECTOR_CLASS, "org.apereo.inspektr.common.web.MockClientInfoCollector");

        ClientInfoThreadLocalFilter filter = new ClientInfoThreadLocalFilter();
        filter.init(filterConfig);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(MockClientInfoCollector.MOCK_CLIENT_IP_HEADER_NAME, MOCK_CLIENT_IP);
        request.addHeader(MockClientInfoCollector.MOCK_USER_AGENT_HEADER_NAME, MOCK_USER_AGENT);

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain filterChain = (servletRequest, servletResponse) -> {
            ClientInfo clientInfo = ClientInfoHolder.getClientInfo();
            assertNotNull("client info must not be null after ClientInfoThreadLocalFilter proceed", clientInfo);
            assertEquals(MOCK_CLIENT_IP, clientInfo.getClientIpAddress());
            assertEquals(MockClientInfoCollector.MOCK_SERVER_IP_ADDRESS, clientInfo.getServerIpAddress());
            assertEquals(MockClientInfoCollector.MOCK_SERVER_GEO_LOCATION, clientInfo.getGeoLocation());
            assertEquals(MOCK_USER_AGENT, clientInfo.getUserAgent());
        };

        filter.doFilter(request, response, filterChain);
    }
}