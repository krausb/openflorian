package de.openflorian.media;

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
