package com.example.dam.salesianos.gestion_recetas.error;

import lombok.Getter;

@Getter
public class TiempoInvalidoException extends RuntimeException {
    private final Integer tiempoPreparacionMin;

    public TiempoInvalidoException(Integer tiempoPreparacionMin) {
        super("Tiempo de preparación inválido: " + tiempoPreparacionMin); // solo para los logs
        this.tiempoPreparacionMin = tiempoPreparacionMin;
    }
}
