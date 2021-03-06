/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.commons.subject;

import org.eclipse.che.api.core.ForbiddenException;

import java.util.Objects;

/**
 * Base implementation of {@link Subject}.
 *
 * @author andrew00x
 */
public class SubjectImpl implements Subject {
    private final String  id;
    private final String  name;
    private final String  token;
    private final boolean isTemporary;

    public SubjectImpl(String name, String id, String token, boolean isTemporary) {
        this.name = name;
        this.id = id;
        this.token = token;
        this.isTemporary = isTemporary;
    }

    @Override
    public String getUserName() {
        return name;
    }

    @Override
    public boolean hasPermission(String domain, String instance, String action) {
        return false;
    }

    @Override
    public void checkPermission(String domain, String instance, String action) throws ForbiddenException {
        throw new ForbiddenException("User is not authorized to perform operation");
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getUserId() {
        return id;
    }

    @Override
    public boolean isTemporary() {
        return isTemporary;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SubjectImpl)) return false;

        SubjectImpl other = (SubjectImpl)obj;

        return Objects.equals(id, other.id)
               && Objects.equals(name, other.name)
               && Objects.equals(token, other.token)
               && isTemporary == other.isTemporary;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(id);
        hash = 31 * hash + Objects.hashCode(name);
        hash = 31 * hash + Objects.hashCode(token);
        hash = 31 * hash + Boolean.hashCode(isTemporary);
        return hash;
    }

    @Override
    public String toString() {
        return "UserImpl{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", token='" + token + '\'' +
               ", isTemporary=" + isTemporary +
               '}';
    }
}
