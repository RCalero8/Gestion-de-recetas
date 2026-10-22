package com.example.dam.salesianos.gestion_recetas.dto;

import com.example.dam.salesianos.gestion_recetas.model.Category;
import com.example.dam.salesianos.gestion_recetas.model.Dificultad;
import com.example.dam.salesianos.gestion_recetas.model.Receta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecetaRequest(
        @NotBlank(message = "{receta.nombre.notblank}")
        @Size(max = 100, message = "{receta.nombre.size}")
        String nombre,

        @NotNull(message = "{receta.tiempoPreparacionMin.null}")
        Integer tiempoPreparacionMin,

        @NotNull(message = "{receta.dificultad.null}")
        Dificultad dificultad,

        @NotNull(message = "{receta.categoria.null}")
        Long categoriaId

) {
    public Receta toEntity(Category categoria) {
        return Receta.builder()
                .nombre(nombre)
                .tiempoPreparacionMin(tiempoPreparacionMin)
                .dificultad(dificultad)
                .categoria(categoria)
                .build();
    }
}