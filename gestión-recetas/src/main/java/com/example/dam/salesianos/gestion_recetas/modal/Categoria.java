package com.example.dam.salesianos.gestion_recetas.modal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nombre;
    private String descripcion;
}
