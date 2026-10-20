package com.example.dam.salesianos.gestion_recetas.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receta {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nombre;
    private Integer tiempoPreparacionMin;
    private Dificultad dificultad;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToMany
    @JoinTable(
            name = "receta_ingredientes",
            joinColumns = @JoinColumn(name = "receta_id"),
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id")

    )
    @Builder.Default
    private Set<Ingrediente> ingredientes = new HashSet<>();
}
