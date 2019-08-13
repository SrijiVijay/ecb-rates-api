package me.vitblokhin.ecbratesapi.controller;

import me.vitblokhin.ecbratesapi.dto.json.ExceptionDto;
import me.vitblokhin.ecbratesapi.exception.InvalidParameterException;
import me.vitblokhin.ecbratesapi.exception.ItemNotFoundException;
import me.vitblokhin.ecbratesapi.exception.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ItemNotFoundException.class})
    public ResponseEntity<Object> notFoundHandler(ItemNotFoundException ex, final WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDto(ex.getMessage()));
    }

    @ExceptionHandler({InvalidParameterException.class})
    public ResponseEntity<Object> invalidParameterHandler(InvalidParameterException ex, final WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDto(ex.getMessage()));
    }

    @ExceptionHandler({Exception.class, ServerException.class})
    public ResponseEntity<Object> errorHandler(Exception ex, final WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionDto(ex.getMessage()));
    }
} // class RestResponseEntityExceptionHandler
