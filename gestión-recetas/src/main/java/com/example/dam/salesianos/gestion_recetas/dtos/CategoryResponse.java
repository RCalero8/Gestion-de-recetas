package com.example.dam.salesianos.gestion_recetas.dtos;

import com.example.dam.salesianos.gestion_recetas.model.Category;

public record CategoryResponse(
        Long id,
        String nombre,
        String descripcion
) {
    public static CategoryResponse of(Category c) {
        return new CategoryResponse(
                c.getId(),
                c.getNombre(),
                c.getDescripcion()
        );
    }
}
