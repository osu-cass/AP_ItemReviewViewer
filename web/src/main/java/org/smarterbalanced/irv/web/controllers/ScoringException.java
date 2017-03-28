/**
 * 
 */
package org.smarterbalanced.irv.web.controllers;

/**
 * @author kthotti
 *
 */
public class ScoringException extends RuntimeException {

	/**
	 * 
	 */
	public ScoringException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ScoringException(String message, String itemId) {
		super(message + " There is an error while scoring the Item: " + itemId);
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * @param cause
	 */
	public ScoringException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ScoringException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ScoringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
