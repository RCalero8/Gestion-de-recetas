package com.example.dam.salesianos.gestion_recetas.dtos;

import com.example.dam.salesianos.gestion_recetas.model.Category;
import jakarta.validation.constraints.*;

public record CategoryRequest(
        @NotBlank(message = "{categoria.nombre.notblank}")
        @Size(max = 100, message = "{categoria.nombre.size}")
        String nombre,

        @Size(max = 255, message = "{categoria.descripcion.size}")
        String descripcion
) {
    public Category toEntity() {
        return Category.builder()
                .nombre(nombre)
                .descripcion(descripcion).build();
    }
}
