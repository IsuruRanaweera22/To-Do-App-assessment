package com.example.todoApp.todoAppBackend.advisor;

import com.example.todoApp.todoAppBackend.exception.NotFoundException;
import com.example.todoApp.todoAppBackend.utils.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardResponse> handleNotFoundException(NotFoundException e){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(404, "not found", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
