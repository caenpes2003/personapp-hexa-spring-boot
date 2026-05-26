package co.edu.javeriana.as.personapp.common.exceptions;

public class UnprocessableEntityException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnprocessableEntityException(String mensaje) {
		super(mensaje);
	}
}
