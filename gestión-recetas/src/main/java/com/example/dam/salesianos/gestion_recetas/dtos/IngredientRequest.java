package com.example.dam.salesianos.gestion_recetas.dtos;

import com.example.dam.salesianos.gestion_recetas.model.Ingredient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IngredientRequest(
        @NotBlank(message = "{ingrediente.nombre.notblank}")
        @Size(max = 100, message = "{ingrediente.nombre.size}")
        String nombre
) {
    public Ingredient toEntity() {
        return Ingredient.builder()
                .nombre(nombre).build();
    }
}
