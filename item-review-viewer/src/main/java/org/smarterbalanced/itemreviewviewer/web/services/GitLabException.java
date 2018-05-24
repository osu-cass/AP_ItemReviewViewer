package org.smarterbalanced.itemreviewviewer.web.services;

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