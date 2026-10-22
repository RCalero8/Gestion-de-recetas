package com.example.dam.salesianos.gestion_recetas.error;

import lombok.Getter;

@Getter
public class TiempoInvalidoException extends RuntimeException {
    private final Integer tiempo;

    public TiempoInvalidoException(Integer tiempo) {
        super();
        this.tiempo = tiempo;
    }

    public Integer getTiempo() {
        return tiempo;
    }
}
