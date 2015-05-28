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
package org.jasig.inspektr.error.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.jasig.inspektr.error.ErrorLogManager;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Spring manageable bean implementing the <code>javax.servlet.Filter</code>
 * interface. This filter should be configured as the top of the web application's
 * filter chain and will record any uncaught exceptions from the chain using
 * the configured {@link ErrorLogManager}.
 * To take advantage of Spring dependency injection,
 * this bean can be specified as the target of a <code>DelegatingFilterProxy</code>.
 * 
 * @author lleung
 * @version $Revision: 1.2 $ $Date: 2007/07/11 20:48:46 $
 * @since 1.0
 */
public class ErrorLoggingFilter extends OncePerRequestFilter {

    @NotNull
    private ErrorLogManager errorLogManager;

    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (final ServletException e) {
            this.errorLogManager.recordError(e);
            throw e;
        } catch (final IOException e) {
            this.errorLogManager.recordError(e);
            throw e;
        } catch (final Throwable t) {
            this.errorLogManager.recordError(t);
            throw new ServletException(t);
        }
    }

    public void setErrorLogManager(final ErrorLogManager errorlogManager) {
        this.errorLogManager = errorlogManager;
    }
}
