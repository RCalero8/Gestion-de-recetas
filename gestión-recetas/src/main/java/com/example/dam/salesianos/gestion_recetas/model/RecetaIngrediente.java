package com.example.dam.salesianos.gestion_recetas.model;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaIngrediente {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receta_id")
    private Receta receta;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id")
    private Ingredient ingrediente;
}
