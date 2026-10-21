package com.example.dam.salesianos.gestion_recetas.error.notFound;

public class IngredientNotFoundException extends EntityNotFoundException {
    public IngredientNotFoundException(Long id) {
        super("ingrediente.noencontrada",id);
    }
}
