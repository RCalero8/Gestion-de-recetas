package com.example.dam.salesianos.gestion_recetas.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingrediente {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nombre;

    @ManyToMany
    @Builder.Default
    private Set<Receta> recetas = new HashSet<>();
}
