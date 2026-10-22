package com.example.dam.salesianos.gestion_recetas.error.notFound;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String claveMensaje;
    private final Long id;

    public EntityNotFoundException(String claveMensaje, Long id) {
        super(claveMensaje + ": " + id); // solo para los logs
        this.claveMensaje = claveMensaje;
        this.id = id;
    }
}
