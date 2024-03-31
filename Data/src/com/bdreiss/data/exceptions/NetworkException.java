package com.bdreiss.data.exceptions;

/**
 * Thrown whenever there is a problem with the network.
 */
public class NetworkException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new instance of network exception.
	 * @param e the original exception
	 */
	public NetworkException(Exception e) {
		super(e);
	}
}
