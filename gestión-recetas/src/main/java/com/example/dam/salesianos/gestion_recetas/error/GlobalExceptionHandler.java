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
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    // 404: categoría, receta o ingrediente no encontrados
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex, Locale locale) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(msg(ex.getClaveMensaje() + ".titulo", null, locale));
        problem.setDetail(msg(ex.getClaveMensaje() + ".detalle", new Object[]{ex.getId()}, locale));
        return problem;
    }

    // 400: tiempo de preparación menor o igual a cero
    @ExceptionHandler(TiempoInvalidoException.class)
    public ProblemDetail handleTiempoInvalido(TiempoInvalidoException ex, Locale locale) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle(msg("receta.tiempo.invalido.titulo", null, locale));
        problem.setDetail(msg("receta.tiempo.invalido.detalle",
                new Object[]{ex.getTiempoPreparacionMin()}, locale));
        return problem;
    }

    // 409: el ingrediente ya estaba en la receta
    @ExceptionHandler(IngredienteYaAnadidoException.class)
    public ProblemDetail handleIngredienteYaAnadido(IngredienteYaAnadidoException ex, Locale locale) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle(msg("receta.ingrediente.duplicado.titulo", null, locale));
        problem.setDetail(msg("receta.ingrediente.duplicado.detalle",
                new Object[]{ex.getReceta(), ex.getIngrediente()}, locale));
        return problem;
    }

    // 409: restricción de la base de datos (nombre único repetido, borrar una categoría con recetas...)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, Locale locale) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle(msg("integridad.error.titulo", null, locale));
        problem.setDetail(msg("integridad.error.detalle", null, locale));
        return problem;
    }

    // 400: validación a nivel de parámetros
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, Locale locale) {
        ProblemDetail result = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, msg("validacion.error.detalle", null, locale));
        result.setTitle(msg("validacion.error.titulo", null, locale));

        List<ApiValidationSubError> subErrors = ex.getConstraintViolations().stream()
                .map(ApiValidationSubError::from)
                .toList();

        result.setProperty("invalid-params", subErrors);
        return result;
    }

    // 400: validación del cuerpo (@Valid sobre los DTOs de entrada)
    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Locale locale = LocaleContextHolder.getLocale();

        ProblemDetail result = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, msg("validacion.error.detalle", null, locale));
        result.setTitle(msg("validacion.error.titulo", null, locale));

        List<ApiValidationSubError> subErrors = ex.getAllErrors().stream()
                .map(error -> ApiValidationSubError.from(error, messageSource, locale))
                .toList();

        result.setProperty("invalid-params", subErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // 400: JSON mal formado o con valores no permitidos (por ejemplo una dificultad que no existe)
    @Override
    protected @Nullable ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Locale locale = LocaleContextHolder.getLocale();

        ProblemDetail result = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, msg("json.ilegible.detalle", null, locale));
        result.setTitle(msg("json.ilegible.titulo", null, locale));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    private String msg(String clave, Object[] argumentos, Locale locale) {
        return messageSource.getMessage(clave, argumentos, locale);
    }

    @Builder
    public record ApiValidationSubError(
            String object,
            String message,
            @JsonInclude(JsonInclude.Include.NON_NULL) String field,
            @JsonInclude(JsonInclude.Include.NON_NULL) Object rejectedValue
    ) {

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