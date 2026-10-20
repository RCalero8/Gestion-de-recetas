package com.example.dam.salesianos.gestion_recetas.dtos;

import com.example.dam.salesianos.gestion_recetas.model.Categoria;

public record CategoriaResponse(
        Long id,
        String nombre,
        String descripcion
) {
    public static CategoriaResponse of(Categoria c) {
        return new CategoriaResponse(
                c.getId(),
                c.getNombre(),
                c.getDescripcion()
        );
    }
}
