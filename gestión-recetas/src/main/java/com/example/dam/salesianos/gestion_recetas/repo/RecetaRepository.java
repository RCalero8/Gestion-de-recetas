package com.example.dam.salesianos.gestion_recetas.repo;

import com.example.dam.salesianos.gestion_recetas.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaRepository extends JpaRepository<Receta, Long> {
    boolean existsByNombre(String nombre);
}
