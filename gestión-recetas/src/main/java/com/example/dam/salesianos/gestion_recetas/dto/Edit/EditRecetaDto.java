package com.example.dam.salesianos.gestion_recetas.dto.Edit;

import com.example.dam.salesianos.gestion_recetas.model.Category;
import com.example.dam.salesianos.gestion_recetas.model.Dificultad;
import com.example.dam.salesianos.gestion_recetas.model.Receta;
import com.example.dam.salesianos.gestion_recetas.validation.NombreRecetaUnico;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear o actualizar una receta")
public record EditRecetaDto(

        @Schema(description = "Nombre único de la receta", example = "Paella valenciana")
        @NotBlank(message = "{receta.nombre.notblank}")
        @Size(max = 100, message = "{receta.nombre.size}")
        @NombreRecetaUnico
        String nombre,

        @Schema(description = "Tiempo de preparación en minutos (debe ser mayor que cero)", example = "45")
        @NotNull(message = "{receta.tiempo.notnull}")
        Integer tiempoPreparacionMin,

        @Schema(description = "Dificultad de la receta", example = "MEDIA")
        @NotNull(message = "{receta.dificultad.notnull}")
        Dificultad dificultad,

        @Schema(description = "Id de una categoría que ya existe", example = "1")
        @NotNull(message = "{receta.categoria.notnull}")
        Long categoriaId
) {
    public Receta to(Category categoria) {
        return Receta.builder()
                .nombre(nombre)
                .tiempoPreparacionMin(tiempoPreparacionMin)
                .dificultad(dificultad)
                .categoria(categoria)
                .build();
    }
}
