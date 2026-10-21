package com.example.dam.salesianos.gestion_recetas.dtos;

import com.example.dam.salesianos.gestion_recetas.model.Ingredient;

public record IngredientResponse(
        Long id,
        String nombre
) {
    public static IngredientResponse of(Ingredient i) {
        return new IngredientResponse(
                i.getId(),
                i.getNombre()
        );
    }
}
