package com.example.dam.salesianos.gestion_recetas.error.notFound;

public class CategoryNotFoundException extends EntityNotFoundException {
    public CategoryNotFoundException(Long id) {
        super("categoria.noencontrada", id);
    }
}
