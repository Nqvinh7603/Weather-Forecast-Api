package com.skyapi.weatherforecast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorDTO handleGenericException(HttpServletRequest request, Exception ex) {
		ErrorDTO error = new ErrorDTO();
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.addError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		error.setPath(request.getServletPath());
		logger.error(ex.getMessage(), ex);
		return error;
	}
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO handleBadRequestException(HttpServletRequest request, Exception ex) {
		ErrorDTO error = new ErrorDTO();
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.addError(ex.getMessage());
		error.setPath(request.getServletPath());
		logger.error(ex.getMessage(), ex);
		return error;
	}
	@Override
	@Nullable
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		
		ErrorDTO error = new ErrorDTO();
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setPath(((ServletWebRequest) request).getRequest().getServletPath());
		List<FieldError> fieldErrors  = ex.getBindingResult().getFieldErrors();
		
		fieldErrors.forEach(fieldError -> {
			error.addError(fieldError.getDefaultMessage());
		});
		return new ResponseEntity<>(error, headers, status);
	}
	
}
