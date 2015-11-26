package de.openflorian.media;

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

/**
 * MediaItem Manager Exception
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class MediaItemManagerException extends Throwable {
	private static final long serialVersionUID = 1204651587653118739L;
	
    protected String[] values;

    /**
     * MediaItemManagerException with message.
     * 
     * @param message I18n key
     */
    public MediaItemManagerException( String message ) {
        super(message);
    }

    /**
     * MediaItemManagerException with message.
     * 
     * @param message I18n key
     * @param value Value for variable substitution
     */
    public MediaItemManagerException( String message,
                        String value ) {
        super(message);
        this.values = new String[] {value};
    }

    /**
     * MediaItemManagerException with message.
     * 
     * @param message I18n key
     * @param values Values for variable substitution
     */
    public MediaItemManagerException( String message,
                        String[] values ) {
        super(message);
        this.values = values;
    }

    /**
     * MediaItemManagerException with message and cause.
     * 
     * @param message I18n key
     * @param cause Causing Exception
     */
    public MediaItemManagerException( String message,
                        Throwable cause ) {
        super(message, cause);
    }

    /**
     * @return the values
     */
    public String[] getValues() {
        if (values != null) return values.clone();
        return null;
    }
	
}
