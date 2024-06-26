package net.berndreiss.trackmyattack.data.exceptions;

/**
 * Gets thrown it entry is not found in DataModel
 * 
 * @author bernd
 *
 */

public class EntryNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public EntryNotFoundException() {
		super("Entry has not been found.");
	}
	
	/**
	 * 
	 * @param message
	 */
	public EntryNotFoundException(String message) {
		super(message);
	}

	//TODO implement EntryNotFoundException
	
}
