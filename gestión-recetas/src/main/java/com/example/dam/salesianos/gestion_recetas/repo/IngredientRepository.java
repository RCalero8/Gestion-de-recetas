package com.example.dam.salesianos.gestion_recetas.repo;


import com.example.dam.salesianos.gestion_recetas.dto.Getter.GetIngredientDetail;
import com.example.dam.salesianos.gestion_recetas.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}

