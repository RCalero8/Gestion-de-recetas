package com.example.dam.salesianos.gestion_recetas.repo;

import com.example.dam.salesianos.gestion_recetas.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepo extends JpaRepository<Categoria, Long> {
    boolean existsByName(String nombre);
}
