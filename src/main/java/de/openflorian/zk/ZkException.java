package de.openflorian.zk;

/**
 * Core Exception
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class ZkException extends Throwable {
	private static final long serialVersionUID = 1204651587653118739L;
	
    protected String[] values;

    /**
     * CoreException with message.
     * 
     * @param message I18n key
     */
    public ZkException( String message ) {
        super(message);
    }

    /**
     * CoreException with message.
     * 
     * @param message I18n key
     * @param value Value for variable substitution
     */
    public ZkException( String message,
                        String value ) {
        super(message);
        this.values = new String[] {value};
    }

    /**
     * CoreException with message.
     * 
     * @param message I18n key
     * @param values Values for variable substitution
     */
    public ZkException( String message,
                        String[] values ) {
        super(message);
        this.values = values;
    }

    /**
     * CoreException with message and cause.
     * 
     * @param message I18n key
     * @param cause Causing Exception
     */
    public ZkException( String message,
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
