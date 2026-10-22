package com.example.dam.salesianos.gestion_recetas.dto.Edit;

import com.example.dam.salesianos.gestion_recetas.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear o actualizar una categoría")
public record EditCategoryDto(

        @Schema(description = "Nombre único de la categoría", example = "Postres")
        @NotBlank(message = "{categoria.nombre.notblank}")
        @Size(max = 100, message = "{categoria.nombre.size}")
        String nombre,

        @Schema(description = "Descripción de la categoría", example = "Dulces y repostería")
        @Size(max = 255, message = "{categoria.descripcion.size}")
        String descripcion
) {
    public Category to() {
        return Category.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .build();
    }
}