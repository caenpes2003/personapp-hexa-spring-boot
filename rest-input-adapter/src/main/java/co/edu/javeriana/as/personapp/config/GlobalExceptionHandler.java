package co.edu.javeriana.as.personapp.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.javeriana.as.personapp.common.exceptions.DuplicateException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.exceptions.UnprocessableEntityException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoExistException.class)
	public ResponseEntity<Map<String, Object>> handleNoExist(NoExistException ex) {
		log.warn("NoExistException: {}", ex.getMessage());
		return build(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage());
	}

	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateException ex) {
		log.warn("DuplicateException: {}", ex.getMessage());
		return build(HttpStatus.CONFLICT, "DUPLICATED", ex.getMessage());
	}

	@ExceptionHandler(InvalidOptionException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidOption(InvalidOptionException ex) {
		log.warn("InvalidOptionException: {}", ex.getMessage());
		return build(HttpStatus.BAD_REQUEST, "INVALID_OPTION", ex.getMessage());
	}

	@ExceptionHandler(UnprocessableEntityException.class)
	public ResponseEntity<Map<String, Object>> handleUnprocessable(UnprocessableEntityException ex) {
		log.warn("UnprocessableEntityException: {}", ex.getMessage());
		return build(HttpStatus.UNPROCESSABLE_ENTITY, "UNPROCESSABLE_ENTITY", ex.getMessage());
	}

	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<Map<String, Object>> handleNumberFormat(NumberFormatException ex) {
		log.warn("NumberFormatException: {}", ex.getMessage());
		return build(HttpStatus.BAD_REQUEST, "INVALID_NUMBER",
				"Un campo numerico recibio un valor no parseable: " + ex.getMessage());
	}

	@ExceptionHandler(java.time.format.DateTimeParseException.class)
	public ResponseEntity<Map<String, Object>> handleDateParse(java.time.format.DateTimeParseException ex) {
		log.warn("DateTimeParseException: {}", ex.getMessage());
		return build(HttpStatus.BAD_REQUEST, "INVALID_DATE",
				"Formato de fecha invalido. Use AAAA-MM-DD.");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
		log.error("Unhandled exception", ex);
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
				"Error interno del servidor. Revise los logs.");
	}

	private ResponseEntity<Map<String, Object>> build(HttpStatus status, String code, String message) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now().toString());
		body.put("status", status.value());
		body.put("code", code);
		body.put("message", message);
		return ResponseEntity.status(status).body(body);
	}
}
