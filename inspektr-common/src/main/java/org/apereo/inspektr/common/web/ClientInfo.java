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

/**
 * Captures information from the HttpServletRequest to log later.
 *
 * @author Scott Battaglia
 * @since 1.0
 */
public class ClientInfo {
    public static ClientInfo EMPTY_CLIENT_INFO = new ClientInfo();

    /** IP Address of the server (local). */
    private final String serverIpAddress;

    /** IP Address of the client (Remote) */
    private final String clientIpAddress;

    private final String geoLocation;

    private final String userAgent;

    private ClientInfo() {
        this.serverIpAddress = this.clientIpAddress = this.geoLocation = this.userAgent = "unknown";
    }

    public ClientInfo(String serverIpAddress, String clientIpAddress, String geoLocation, String userAgent) {
        this.serverIpAddress = serverIpAddress;
        this.clientIpAddress = clientIpAddress;
        this.geoLocation = geoLocation;
        this.userAgent = userAgent;
    }

    public String getServerIpAddress() {
        return this.serverIpAddress;
    }

    public String getClientIpAddress() {
        return this.clientIpAddress;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
