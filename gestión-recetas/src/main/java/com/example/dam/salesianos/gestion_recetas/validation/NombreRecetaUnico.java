package com.example.dam.salesianos.gestion_recetas.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NombreRecetaUnicoValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NombreRecetaUnico {

    String message() default "{receta.nombre.duplicado}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
