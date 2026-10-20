package com.example.dam.salesianos.gestion_recetas.error.notFound;

public class EntityNotFoundException extends RuntimeException{
    private final String claveMensaje;
    private final Long id;

    public EntityNotFoundException(String claveMensaje, Long id){
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
