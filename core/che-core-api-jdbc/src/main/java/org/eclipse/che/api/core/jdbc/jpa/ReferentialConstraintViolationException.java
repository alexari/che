package org.eclipse.che.api.core.jdbc.jpa;

import org.eclipse.che.api.core.jdbc.DBErrorCode;

/**
 * @author Anton Korneta
 */
public class ReferentialConstraintViolationException extends DetailedRollbackException {

    public ReferentialConstraintViolationException(String message, Throwable cause) {
        super(message, cause, DBErrorCode.REFERENTIAL_CONSTRAINT_VIOLATION);
    }
}
