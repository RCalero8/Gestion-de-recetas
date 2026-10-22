package com.example.dam.salesianos.gestion_recetas.dto.Getter;

import com.example.dam.salesianos.gestion_recetas.model.RecetaIngrediente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ingrediente dentro de una receta, con su cantidad y unidad")
public record GetRecetaIngredienteDetail(

        @Schema(description = "Ingrediente")
        GetIngredientDetail ingrediente,

        @Schema(description = "Cantidad (puede ser nula)", example = "200")
        Double cantidad,

        @Schema(description = "Unidad de medida (puede ser nula)", example = "gramos")
        String unidad
) {

    public static GetRecetaIngredienteDetail of(RecetaIngrediente ri) {
        return new GetRecetaIngredienteDetail(
                GetIngredientDetail.of(ri.getIngrediente()),
                ri.getCantidad(),
                ri.getUnidad()
        );
    }
}