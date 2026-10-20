package com.example.dam.salesianos.gestion_recetas.dtos;

import com.example.dam.salesianos.gestion_recetas.model.Categoria;
import jakarta.validation.constraints.*;

public record CategoriaRequest(
        @NotBlank(message = "{categoria.nombre.notblank}")
        @Size(max = 255, message = "{categoria.nombre.size}")
        String nombre,

        @Size(max = 255, message = "{categoria.descripcion.size}")
        String descripcion
) {
    public Categoria toEntity() {
        return Categoria.builder()
                .nombre(nombre)
                .descripcion(descripcion).build();
    }
}
