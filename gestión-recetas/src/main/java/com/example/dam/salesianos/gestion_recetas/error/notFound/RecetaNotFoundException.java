package com.example.dam.salesianos.gestion_recetas.error.notFound;

public class RecetaNotFoundException extends EntityNotFoundException {
    public RecetaNotFoundException(Long id) {
        super("receta.noencontrada", id);
    }
}
