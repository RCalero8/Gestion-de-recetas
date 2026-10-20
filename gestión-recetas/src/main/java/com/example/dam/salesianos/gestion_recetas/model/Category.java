package com.example.dam.salesianos.gestion_recetas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nombre;
    private String descripcion;


}
