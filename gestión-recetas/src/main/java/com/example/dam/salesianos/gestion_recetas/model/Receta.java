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
    private Category categoria;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true )
    @Builder.Default
    private Set<RecetaIngrediente> ingredientes = new HashSet<>();

}
