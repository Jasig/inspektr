/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apereo.inspektr.common.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Creates a ClientInfo object and passes it to the {@link ClientInfoHolder}
 * <p>
 * If one provides an alternative IP Address Header (i.e. init-param "alternativeIpAddressHeader"), the client
 * IP address will be read from that instead.
 *
 * @author Scott Battaglia
 * @since 1.0
 */
public class ClientInfoThreadLocalFilter implements Filter {
    public static final String CONST_CLIENT_INFO_COLLECTOR_CLASS = "clientInfoCollectorClass";
    public static final String CONST_IP_ADDRESS_HEADER = "alternativeIpAddressHeader";
    public static final String CONST_SERVER_IP_ADDRESS_HEADER = "alternateServerAddrHeaderName";
    public static final String CONST_USE_SERVER_HOST_ADDRESS = "useServerHostAddress";

    private ClientInfoCollector clientInfoCollector;

    @Override
    public void destroy() {
        // nothing to do here
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        try {
            ClientInfo clientInfo = clientInfoCollector.collect((HttpServletRequest) request);
            ClientInfoHolder.setClientInfo(clientInfo);

            filterChain.doFilter(request, response);
        } finally {
            ClientInfoHolder.clear();
        }
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        String clientInfoCollectorClassName = filterConfig.getInitParameter(CONST_CLIENT_INFO_COLLECTOR_CLASS);
        if (clientInfoCollectorClassName != null && !clientInfoCollectorClassName.isEmpty()) {
            this.clientInfoCollector = newInstanceOfClientInfoCollectorByClassName(clientInfoCollectorClassName);
        } else {
            String alternateLocalAddrHeaderName = filterConfig.getInitParameter(CONST_IP_ADDRESS_HEADER);
            String alternateServerAddrHeaderName = filterConfig.getInitParameter(CONST_SERVER_IP_ADDRESS_HEADER);
            String useServerHostAddr = filterConfig.getInitParameter(CONST_USE_SERVER_HOST_ADDRESS);
            boolean useServerHostAddress = false;
            if (useServerHostAddr != null && !useServerHostAddr.isEmpty()) {
                useServerHostAddress = Boolean.parseBoolean(useServerHostAddr);
            }

            DefaultClientInfoCollector clientInfoCollector = new DefaultClientInfoCollector();
            clientInfoCollector.setAlternateLocalAddrHeaderName(alternateLocalAddrHeaderName);
            clientInfoCollector.setAlternateServerAddrHeaderName(alternateServerAddrHeaderName);
            clientInfoCollector.setUseServerHostAddress(useServerHostAddress);
            this.clientInfoCollector = clientInfoCollector;
        }
    }

    private static ClientInfoCollector newInstanceOfClientInfoCollectorByClassName(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(className + " class not found", e);
        }

        try {
            return (ClientInfoCollector) clazz.getConstructor().newInstance();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(className + " class is not a ClientInfoCollector", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating new instance of " + className, e);
        }
    }
}
