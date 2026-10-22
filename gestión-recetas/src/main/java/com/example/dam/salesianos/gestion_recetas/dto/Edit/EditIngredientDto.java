package com.example.dam.salesianos.gestion_recetas.dto.Edit;

import com.example.dam.salesianos.gestion_recetas.model.Ingredient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear un ingrediente")
public record EditIngredientDto(

        @Schema(description = "Nombre único del ingrediente", example = "Harina")
        @NotBlank(message = "{ingrediente.nombre.notblank}")
        @Size(max = 100, message = "{ingrediente.nombre.size}")
        String nombre
) {

    public Ingredient to() {
        return Ingredient.builder()
                .nombre(nombre)
                .build();
    }
}
