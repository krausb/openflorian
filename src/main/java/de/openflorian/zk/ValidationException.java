package de.openflorian.zk;

import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;

/**
 * CoreException for the hibernate validator
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class ValidationException extends ZkException {
    private static final long serialVersionUID = 117458168015973951L;
    private InvalidValue[] invalidValues;

    public ValidationException( InvalidStateException cause ) {
        super(cause.getMessage(), cause);
        this.invalidValues = cause.getInvalidValues();
    }

    public ValidationException( String message ) {
        super(message);
    }

    /**
     * @return the invalidValues
     */
    public InvalidValue[] getInvalidValues() {
        if (invalidValues != null) return invalidValues.clone();
        return null;
    }
}
