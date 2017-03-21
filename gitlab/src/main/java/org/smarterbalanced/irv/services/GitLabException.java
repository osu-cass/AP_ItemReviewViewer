/**
 * 
 */
package org.smarterbalanced.irv.services;

/**
 * @author kthotti
 *
 */
public class GitLabException extends RuntimeException {

	public GitLabException(String message) {
		super(message);
	}

	public GitLabException(Exception exp) {
		super(exp);
	}

	public GitLabException(String message, Exception exp) {
		super(message, exp);
	}

}


