package com.example.dam.salesianos.gestion_recetas.error;

import com.example.dam.salesianos.gestion_recetas.error.notFound.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler
    public ProblemDetail EntidadesNoEncontradas(EntityNotFoundException ex, Locale locale) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(messageSource.getMessage(
                ex.getClaveMensaje() + ".titulo",
                null,
                locale));
        problem.setDetail(messageSource.getMessage(
                ex.getClaveMensaje() + ".detalle",
                new Object[]{ex.getId()},
                locale));
        return problem;
    }

    //Manejo de error de validaciób
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "La solicitud contiene errores de validación"
        );
        problem.setTitle("Error de validación");

        // Extrae cada campo y su mensaje de error
        Map<String, String> errores = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        // Añade la lista de errores en una propiedad personalizada dentro del ProblemDetail
        problem.setProperty("invalidParams", errores);

        return ResponseEntity.badRequest().body(problem);
    }
}