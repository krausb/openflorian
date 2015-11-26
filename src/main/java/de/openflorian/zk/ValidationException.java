package de.openflorian.zk;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

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
