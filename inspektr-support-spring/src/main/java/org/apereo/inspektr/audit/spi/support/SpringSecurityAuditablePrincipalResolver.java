/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apereo.inspektr.audit.spi.support;

import org.aspectj.lang.JoinPoint;
import org.apereo.inspektr.common.spi.PrincipalResolver;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Resolves the principal name to the one provided by Spring Security.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 0.7.0
 *
 */
public class SpringSecurityAuditablePrincipalResolver implements PrincipalResolver {

    public String resolveFrom(final JoinPoint auditableTarget, final Object retval) {
        return getFromSecurityContext();
    }

    public String resolveFrom(final JoinPoint auditableTarget, final Exception exception) {
        return getFromSecurityContext();
    }

    public String resolve() {
        return getFromSecurityContext();
    }

    private String getFromSecurityContext() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext == null) {
            return UNKNOWN_USER;
        }

        if (securityContext.getAuthentication() == null) {
            return UNKNOWN_USER;
        }

        final String subject = securityContext.getAuthentication().getName();
        if (subject == null) {
            return UNKNOWN_USER;
        }
        return subject;
    }

}
