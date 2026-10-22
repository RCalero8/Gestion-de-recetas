package com.example.dam.salesianos.gestion_recetas.utils;

import com.example.dam.salesianos.gestion_recetas.model.*;
import com.example.dam.salesianos.gestion_recetas.repo.CategoryRepository;
import com.example.dam.salesianos.gestion_recetas.repo.IngredientRepository;
import com.example.dam.salesianos.gestion_recetas.repo.RecetaRepositoy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeed {
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final RecetaRepositoy recetaRepository;

    @PostConstruct
    public void initData() {
        //Categorias
        Category sopas = Category.builder()
                .nombre("Sopas")
                .descripcion("Caldos, cremas y sopas")
                .build();
        Category carnes = Category.builder()
                .nombre("Carnes")
                .descripcion("Platos con carne")
                .build();
        categoryRepository.saveAll(List.of(sopas, carnes));

        //Ingredientes
        Ingredient pollo = Ingredient.builder()
                .nombre("Pollo")
                .build();
        Ingredient agua = Ingredient.builder()
                .nombre("Agua")
                .build();
        Ingredient sal = Ingredient.builder()
                .nombre("Sal")
                .build();
        ingredientRepository.saveAll(List.of(pollo, agua, sal));
        //Receta
        Receta caldo = Receta.builder()
                .nombre("Caldo de pollo")
                .tiempoPreparacionMin(60)
                .dificultad(Dificultad.FACIL)
                .categoria(sopas)
                .build();
        //All
        caldo.getIngredientes().add(RecetaIngrediente.builder()
                .receta(caldo).ingrediente(pollo).cantidad(500.0).unidad("gramos").build());
        caldo.getIngredientes().add(RecetaIngrediente.builder()
                .receta(caldo).ingrediente(agua).cantidad(2.0).unidad("litros").build());

        recetaRepository.save(caldo);
    }
}
