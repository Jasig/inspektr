package org.apereo.inspektr.common.web;

import javax.servlet.http.HttpServletRequest;

public interface ClientInfoCollector {
    ClientInfo collect(HttpServletRequest request);
}
