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
package org.apereo.inspektr.error.support;

import java.util.List;

import org.apereo.inspektr.common.spi.PrincipalResolver;
import org.apereo.inspektr.error.ErrorLogManager;
import org.apereo.inspektr.error.ErrorReporter;

/**
 * An <code>ErrorLogManager</code> implementation.
 * 
 * @author lleung
 * @version $Revision: 1.2 $ $Date: 2007/07/11 20:48:47 $
 * @since 1.0
 */
public class DefaultErrorLogManager implements ErrorLogManager {

    /**
     * The application identifier to use when recording errors.
     */
    private String applicationCode;

    /**
     * The resolver for a description of the principal associated with the error.
     */
    private PrincipalResolver currentContextPrincipalResolver;

    /**
     * The ErrorReporters to use to report the errors.
     */
    private List<ErrorReporter> errorReporters;

    /**
     * Constructs a new <code>DefaultErrorLogManager</code>.
     *
     * @param applicationCode	The application identifier to use when reporting errors.
     * @param errorReporters	A list of <code>ErrorReporters</code> to use for reporting errors.
     */
    public DefaultErrorLogManager(final String applicationCode, final List<ErrorReporter> errorReporters) {
        this.applicationCode = applicationCode;
        this.errorReporters = errorReporters;
    }

    /**
     * Sets the <code>currentContextPrincipalResolver</code>. The resolver will be
     * used to resolve current context information on the principal.
     *
     * @param resolver	the <code>CurrentContextPrincipalResolver</code> to use.
     */
    public void setCurrentContextPrincipalResolver(PrincipalResolver resolver) {
        this.currentContextPrincipalResolver = resolver;
    }

    @Override
    public void recordError(final String errorDescription) {
        String principal = resolvePrincipal();
        for (ErrorReporter r : this.errorReporters) {
            r.reportError(this.applicationCode, principal, errorDescription);
        }
    }

    @Override
    public void recordError(final Throwable throwable) {
        String principal = resolvePrincipal();
        for (final ErrorReporter r : this.errorReporters) {
            r.reportError(this.applicationCode, principal, throwable);
        }
    }

    @Override
    public List<ErrorReporter> getErrorReporters() {
        return this.errorReporters;
    }

    private String resolvePrincipal() {
        return (this.currentContextPrincipalResolver==null) ?
                null :
                this.currentContextPrincipalResolver.resolve();
    }

}
