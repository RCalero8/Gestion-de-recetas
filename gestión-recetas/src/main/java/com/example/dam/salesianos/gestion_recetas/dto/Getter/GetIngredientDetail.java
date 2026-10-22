package com.example.dam.salesianos.gestion_recetas.dto.Getter;

import com.example.dam.salesianos.gestion_recetas.model.Ingredient;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ingrediente")
public record GetIngredientDetail(

        @Schema(description = "Identificador del ingrediente", example = "1")
        Long id,

        @Schema(description = "Nombre del ingrediente", example = "Harina")
        String nombre
) {

    public static GetIngredientDetail of(Ingredient i) {
        return new GetIngredientDetail(i.getId(), i.getNombre());
    }
}
