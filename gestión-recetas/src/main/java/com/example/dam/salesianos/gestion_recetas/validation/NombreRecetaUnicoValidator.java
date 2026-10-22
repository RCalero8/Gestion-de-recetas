package com.example.dam.salesianos.gestion_recetas.validation;

import com.example.dam.salesianos.gestion_recetas.repo.RecetaRepositoy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

public class NombreRecetaUnicoValidator implements ConstraintValidator<NombreRecetaUnico, String> {

    private final RecetaRepositoy recetaRepository;

    public NombreRecetaUnicoValidator(RecetaRepositoy recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @Override
    public boolean isValid(String nombre, ConstraintValidatorContext context) {
        // Los nulos y vacíos los validan @NotBlank y @Size
        if (nombre == null || nombre.isBlank()) {
            return true;
        }

        Long idRuta = idDeLaRuta();
        boolean duplicado = idRuta == null
                ? recetaRepository.existsByNombreIgnoreCase(nombre.trim())
                : recetaRepository.existsByNombreIgnoreCaseAndIdNot(nombre.trim(), idRuta);

        return !duplicado;
    }

    @SuppressWarnings("unchecked")
    private Long idDeLaRuta() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        Map<String, String> variables = (Map<String, String>) attrs.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (variables == null || !variables.containsKey("id")) {
            return null;
        }
        try {
            return Long.valueOf(variables.get("id"));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
