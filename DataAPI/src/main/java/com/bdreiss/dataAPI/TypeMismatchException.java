package main.java.com.bdreiss.dataAPI;

public class TypeMismatchException extends Exception {

	private static final long serialVersionUID = 1L;

	public TypeMismatchException() {
		super("Wrong type provided.");
	}
	
	public TypeMismatchException(String message) {
		super(message);
	}
}
