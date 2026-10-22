package com.example.dam.salesianos.gestion_recetas.repo;

import com.example.dam.salesianos.gestion_recetas.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
