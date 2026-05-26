package co.edu.javeriana.as.personapp.common.exceptions;

public class DuplicateException extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateException(String mensaje) {
		super(mensaje);
	}
}
