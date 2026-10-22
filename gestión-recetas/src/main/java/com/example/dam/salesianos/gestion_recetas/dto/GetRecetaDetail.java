package com.example.dam.salesianos.gestion_recetas.dto;

import com.example.dam.salesianos.gestion_recetas.model.Dificultad;
import com.example.dam.salesianos.gestion_recetas.model.Receta;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Receta con su categoría y la lista de sus ingredientes")
public record GetRecetaDetail(

        @Schema(description = "Identificador de la receta", example = "1")
        Long id,

        @Schema(description = "Nombre de la receta", example = "Paella valenciana")
        String nombre,

        @Schema(description = "Tiempo de preparación en minutos", example = "45")
        Integer tiempoPreparacionMin,

        @Schema(description = "Dificultad", example = "MEDIA")
        Dificultad dificultad,

        @Schema(description = "Categoría a la que pertenece")
        GetCategoryDetail categoria,

        @Schema(description = "Ingredientes de la receta (lista vacía si todavía no tiene)")
        List<GetRecetaIngredienteDetail> ingredientes
) {

    /** Transformación entidad a DTO, con control de nulos (DTO anidado de ingredientes). */
    public static GetRecetaDetail of(Receta r) {
        GetCategoryDetail categoria = r.getCategoria() == null
                ? null
                : GetCategoryDetail.of(r.getCategoria());

        List<GetRecetaIngredienteDetail> ingredientes = r.getIngredientes() == null
                ? List.of()
                : r.getIngredientes().stream()
                .map(GetRecetaIngredienteDetail::of)
                .toList();

        return new GetRecetaDetail(
                r.getId(),
                r.getNombre(),
                r.getTiempoPreparacionMin(),
                r.getDificultad(),
                categoria,
                ingredientes
        );
    }
}
