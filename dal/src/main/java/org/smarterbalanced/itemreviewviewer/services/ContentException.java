/**
 * 
 */
package org.smarterbalanced.itemreviewviewer.services;

/**
 * @author kthotti
 *
 */
public class ContentException extends RuntimeException {

	public ContentException(String message) {
		super(message);
	}

	public ContentException(Exception exp) {
		super(exp);
	}

	public ContentException(String message, Exception exp) {
		super(message, exp);
	}

}


