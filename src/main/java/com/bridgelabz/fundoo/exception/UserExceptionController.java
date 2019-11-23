package com.bridgelabz.fundoo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionController {
	@ExceptionHandler(value = UserExceptions.class)
	  public ResponseEntity<Object> exception(UserExceptions exception) {
	     return new ResponseEntity<Object>( exception.getMessage(),HttpStatus.NOT_FOUND);

	  }

}
