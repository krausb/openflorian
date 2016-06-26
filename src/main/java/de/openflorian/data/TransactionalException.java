package de.openflorian.data;

/**
 * Transactional Exception
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class TransactionalException extends Exception {
	private static final long serialVersionUID = 6291477662207224304L;

	public TransactionalException() {
	}

	public TransactionalException(String message) {
		super(message);
	}

	public TransactionalException(Throwable cause) {
		super(cause);
	}

	public TransactionalException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionalException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
