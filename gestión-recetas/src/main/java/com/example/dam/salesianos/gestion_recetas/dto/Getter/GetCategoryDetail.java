package com.example.dam.salesianos.gestion_recetas.dto.Getter;

import com.example.dam.salesianos.gestion_recetas.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categoría de recetas")
public record GetCategoryDetail(

        @Schema(description = "Identificador de la categoría", example = "1")
        Long id,

        @Schema(description = "Nombre de la categoría", example = "Postres")
        String nombre,

        @Schema(description = "Descripción de la categoría", example = "Dulces y repostería")
        String descripcion
) {

    /** Transformación entidad a DTO de salida. */
    public static GetCategoryDetail of(Category c) {
        return new GetCategoryDetail(c.getId(), c.getNombre(), c.getDescripcion());
    }
}
