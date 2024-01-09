package com.bdreiss.dataAPI.exceptions;

/**
 * 
 */
public class TypeMismatchException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public TypeMismatchException() {
		super("Wrong type provided.");
	}
	
	/**
	 * 
	 * @param message
	 */
	public TypeMismatchException(String message) {
		super(message);
	}
}
