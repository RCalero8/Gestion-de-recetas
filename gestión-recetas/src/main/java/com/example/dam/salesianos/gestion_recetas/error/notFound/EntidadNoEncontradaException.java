package com.example.dam.salesianos.gestion_recetas.error.notFound;

public class EntidadNoEncontradaException extends RuntimeException{
    private final String claveMensaje;
    private final Long id;

    public EntidadNoEncontradaException(String claveMensaje, Long id){
        super(claveMensaje + ": " + id);
        this.claveMensaje = claveMensaje;
        this.id = id;
    }

    public String getClaveMensaje() {
        return claveMensaje;
    }
    public Long getId() {
        return id;
    }
}
