package org.apereo.inspektr.common.web;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;

public class DefaultClientInfoCollector implements ClientInfoCollector {
    private String alternateLocalAddrHeaderName;
    private boolean useServerHostAddress;
    private String alternateServerAddrHeaderName;

    public void setAlternateLocalAddrHeaderName(String alternateLocalAddrHeaderName) {
        this.alternateLocalAddrHeaderName = alternateLocalAddrHeaderName;
    }

    public void setUseServerHostAddress(boolean useServerHostAddress) {
        this.useServerHostAddress = useServerHostAddress;
    }

    public void setAlternateServerAddrHeaderName(String alternateServerAddrHeaderName) {
        this.alternateServerAddrHeaderName = alternateServerAddrHeaderName;
    }

    @Override
    public ClientInfo collect(HttpServletRequest request) {
        String serverIpAddress;
        String clientIpAddress;
        String geoLocation;
        String userAgent;

        try {
            serverIpAddress = request != null ? request.getLocalAddr() : null;
            clientIpAddress = request != null ? request.getRemoteAddr() : null;

            if (request == null) {
                geoLocation = "unknown";
                userAgent = "unknown";
            } else {
                if (useServerHostAddress) {
                    serverIpAddress = Inet4Address.getLocalHost().getHostAddress();
                } else if (alternateServerAddrHeaderName != null && !alternateServerAddrHeaderName.isEmpty()) {
                    serverIpAddress = request.getHeader(alternateServerAddrHeaderName) != null
                            ? request.getHeader(alternateServerAddrHeaderName) : request.getLocalAddr();
                }

                if (alternateLocalAddrHeaderName != null && !alternateLocalAddrHeaderName.isEmpty()) {
                    clientIpAddress = request.getHeader(alternateLocalAddrHeaderName) != null ? request.getHeader
                            (alternateLocalAddrHeaderName) : request.getRemoteAddr();
                }
                String header = request.getHeader("user-agent");
                userAgent = header == null ? "unknown" : header;
                String geo = request.getParameter("geolocation");
                if (geo == null) {
                    geo = request.getHeader("geolocation");
                }
                geoLocation = geo == null ? "unknown" : geo;
            }

            serverIpAddress = serverIpAddress == null ? "unknown" : serverIpAddress;
            clientIpAddress = clientIpAddress == null ? "unknown" : clientIpAddress;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        return new ClientInfo(serverIpAddress, clientIpAddress, geoLocation, userAgent);
    }
}
