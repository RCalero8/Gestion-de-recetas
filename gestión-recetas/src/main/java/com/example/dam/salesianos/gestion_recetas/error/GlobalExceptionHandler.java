package com.example.dam.salesianos.gestion_recetas.error;

import com.example.dam.salesianos.gestion_recetas.error.notFound.EntidadNoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler
    public ProblemDetail handleProductNotFound(EntidadNoEncontradaException ex) {


    }
}