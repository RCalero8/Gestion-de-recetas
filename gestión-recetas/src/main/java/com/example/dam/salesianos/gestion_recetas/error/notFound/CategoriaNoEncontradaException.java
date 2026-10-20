package com.example.dam.salesianos.gestion_recetas.error.notFound;

public class CategoriaNoEncontradaException extends EntidadNoEncontradaException {
    public CategoriaNoEncontradaException(Long id) {
        super("categoria.noencontrada",id);
    }

}
