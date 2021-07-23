package org.apereo.inspektr.common.web;

import javax.servlet.http.HttpServletRequest;

public class MockClientInfoCollector implements ClientInfoCollector {
    public static final String MOCK_SERVER_IP_ADDRESS = "321.321.321.321";
    public static final String MOCK_SERVER_GEO_LOCATION = "MARS";
    public static final String MOCK_CLIENT_IP_HEADER_NAME = "X-Test-Client-IP-Address";
    public static final String MOCK_USER_AGENT_HEADER_NAME = "X-Test-User-Agent";

    @Override
    public ClientInfo collect(HttpServletRequest request) {
        final String clientIpAddress = request.getHeader(MOCK_CLIENT_IP_HEADER_NAME);
        final String userAgent = request.getHeader(MOCK_USER_AGENT_HEADER_NAME);

        return new ClientInfo(MOCK_SERVER_IP_ADDRESS, clientIpAddress, MOCK_SERVER_GEO_LOCATION, userAgent);
    }
}
