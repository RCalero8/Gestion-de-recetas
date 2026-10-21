package com.example.dam.salesianos.gestion_recetas.dto;

import com.example.dam.salesianos.gestion_recetas.model.Category;
import com.example.dam.salesianos.gestion_recetas.model.Dificultad;
import com.example.dam.salesianos.gestion_recetas.model.Receta;

import java.util.List;

public record RecetaResponse(
        Long id,
        String nombre,
        Integer tiempoPreparacionMin,
        Dificultad dificultad,
        CategoryResponse categoria,
        List<IngredientResponse> ingredientes
) {
    public static RecetaResponse of(Receta r) {
        //Si categoria viene nula
        CategoryResponse categoriaDto = (r.getCategoria() != null)
                ? CategoryResponse.of(r.getCategoria())
                : null;
        //Si Ingredientes si es vacia
        List<IngredientResponse> ingredientesDto = (r.getIngredientes() != null)
                ?r.getIngredientes().stream()
                .map(IngredientResponse::of)
                .toList()
                : List.of();

        return new RecetaResponse(
                r.getId(),
                r.getNombre(),
                r.getTiempoPreparacionMin(),
                r.getDificultad(),
                categoriaDto,
                ingredientesDto
        );
    }

}
