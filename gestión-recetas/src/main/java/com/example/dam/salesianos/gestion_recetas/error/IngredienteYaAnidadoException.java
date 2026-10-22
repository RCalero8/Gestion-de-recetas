package com.example.dam.salesianos.gestion_recetas.error;

import lombok.Getter;

@Getter
public class IngredienteYaAnidadoException extends RuntimeException {
    private final String receta;
    private final String ingrediente;

    public IngredienteYaAnidadoException(String receta, String ingrediente) {
        super("El ingrediente " + ingrediente + " ya está en la receta " + receta); // solo para los logs
        this.receta = receta;
        this.ingrediente = ingrediente;
    }
}