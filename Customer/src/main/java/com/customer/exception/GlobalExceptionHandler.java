package com.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

	/*
	 * @ExceptionHandler(MethodArgumentNotValidException.class) public
	 * ResponseEntity<Map<String, Object>>
	 * handleValidationExceptions(MethodArgumentNotValidException ex) { List<String>
	 * errors = ex.getBindingResult() .getFieldErrors() .stream() .map(err ->
	 * err.getDefaultMessage()) .collect(Collectors.toList());
	 * 
	 * Map<String, Object> body = new HashMap<>(); body.put("timestamp",
	 * LocalDateTime.now()); body.put("status", HttpStatus.BAD_REQUEST.value());
	 * body.put("errors", errors);
	 * 
	 * return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);}
	 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                error -> error.getField(),  // field name
                error -> error.getDefaultMessage(), // message
                (existing, replacement) -> existing  // in case of duplicate field names
            ));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("errors", fieldErrors); // field: message

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
