/**
 * 
 */
package com.geh.frontend.controllers;

/**
 * Exception wrapping all exceptions in the frontend
 *
 * @author Vera Boutchkova
 */
public class FrontendException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FrontendException(String message) {
        super(message);
    }
	
	public FrontendException(String message, Throwable cause) {
        super(message, cause);
    }
}
