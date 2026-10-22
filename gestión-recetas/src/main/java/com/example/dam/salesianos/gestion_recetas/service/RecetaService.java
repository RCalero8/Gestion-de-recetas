package com.example.dam.salesianos.gestion_recetas.service;

import com.example.dam.salesianos.gestion_recetas.repo.CategoryRepository;
import com.example.dam.salesianos.gestion_recetas.repo.IngredientRepository;
import com.example.dam.salesianos.gestion_recetas.repo.RecetaRepositoy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecetaService {

   private final RecetaRepositoy recetaRepositoy;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;

  
}
