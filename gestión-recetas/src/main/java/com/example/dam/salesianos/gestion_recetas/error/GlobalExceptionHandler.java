package com.example.dam.salesianos.gestion_recetas.error;

import com.example.dam.salesianos.gestion_recetas.error.notFound.EntityNotFoundException;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    //404 tiempo invalido
    @ExceptionHandler(TiempoInvalidoException.class)
    public ProblemDetail handleTiempoInvalido(TiempoInvalidoException ex, Locale locale) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle(messageSource.getMessage(
                "receta.tiempo.invalido.titulo", null, locale));
        problem.setDetail(messageSource.getMessage(
                "receta.tiempo.invalido.detalle",
                new Object[]{ex.getTiempo()}, locale));
        return problem;
    }
    // 404: Categoría, Receta o Ingrediente no encontrados
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex, Locale locale) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(messageSource.getMessage(
                ex.getClaveMensaje() + ".titulo", null, locale));
        problem.setDetail(messageSource.getMessage(
                ex.getClaveMensaje() + ".detalle", new Object[]{ex.getId()}, locale));
        return problem;
    }



    // 400: validación a nivel de parámetros (path, query...)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, Locale locale) {
        ProblemDetail result = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                messageSource.getMessage("validacion.error.detalle", null, locale));
        result.setTitle(messageSource.getMessage("validacion.error.titulo", null, locale));

        List<ApiValidationSubError> subErrors = ex.getConstraintViolations().stream()
                .map(ApiValidationSubError::from)
                .toList();

        result.setProperty("invalid-params", subErrors);
        return result;
    }

    // 400: validación del cuerpo (@Valid sobre los DTOs)
    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Locale locale = LocaleContextHolder.getLocale();

        ProblemDetail result = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                messageSource.getMessage("validacion.error.detalle", null, locale));
        result.setTitle(messageSource.getMessage("validacion.error.titulo", null, locale));

        List<ApiValidationSubError> subErrors = ex.getAllErrors().stream()
                .map(error -> ApiValidationSubError.from(error, messageSource, locale))
                .toList();

        result.setProperty("invalid-params", subErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @Builder
    record ApiValidationSubError(
            String object,
            String message,
            @JsonInclude(JsonInclude.Include.NON_NULL) String field,
            @JsonInclude(JsonInclude.Include.NON_NULL) Object rejectedValue
    ) {

        public ApiValidationSubError(String object, String message) {
            this(object, message, null, null);
        }

        public static ApiValidationSubError from(ObjectError error, MessageSource messageSource, Locale locale) {
            String mensajeTraducido = messageSource.getMessage(error, locale);

            if (error instanceof FieldError fieldError) {
                return ApiValidationSubError.builder()
                        .object(fieldError.getObjectName())
                        .message(mensajeTraducido)
                        .field(fieldError.getField())
                        .rejectedValue(fieldError.getRejectedValue())
                        .build();
            }
            return ApiValidationSubError.builder()
                    .object(error.getObjectName())
                    .message(mensajeTraducido)
                    .build();
        }

        public static ApiValidationSubError from(ConstraintViolation<?> v) {
            return ApiValidationSubError.builder()
                    .message(v.getMessage())
                    .rejectedValue(v.getInvalidValue())
                    .object(v.getRootBeanClass().getSimpleName())
                    .field(getFieldName(v))
                    .build();
        }

        private static String getFieldName(ConstraintViolation<?> v) {
            String field = null;
            for (Path.Node node : v.getPropertyPath()) {
                if (node.getKind() == ElementKind.PROPERTY || node.getKind() == ElementKind.PARAMETER) {
                    field = node.getName() != null ? node.getName() : node.toString();
                }
            }
            return field;
        }
    }
}