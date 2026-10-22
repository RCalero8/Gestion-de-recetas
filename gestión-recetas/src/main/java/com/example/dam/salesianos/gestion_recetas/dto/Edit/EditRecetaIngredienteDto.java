package com.example.dam.salesianos.gestion_recetas.dto.Edit;

import com.example.dam.salesianos.gestion_recetas.model.Ingredient;
import com.example.dam.salesianos.gestion_recetas.model.Receta;
import com.example.dam.salesianos.gestion_recetas.model.RecetaIngrediente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para añadir un ingrediente existente a una receta")
public record EditRecetaIngredienteDto(

        @Schema(description = "Id de un ingrediente que ya existe", example = "1")
        @NotNull(message = "{receta.ingrediente.id.notnull}")
        Long ingredienteId,

        @Schema(description = "Cantidad (opcional)", example = "200")
        @Positive(message = "{receta.ingrediente.cantidad.positive}")
        Double cantidad,

        @Schema(description = "Unidad de medida (opcional)", example = "gramos")
        @Size(max = 50, message = "{receta.ingrediente.unidad.size}")
        String unidad
) {
    public RecetaIngrediente to(Receta receta, Ingredient ingrediente) {
        return RecetaIngrediente.builder()
                .receta(receta)
                .ingrediente(ingrediente)
                .cantidad(cantidad)
                .unidad(unidad)
                .build();
    }
}
