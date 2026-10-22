package com.example.dam.salesianos.gestion_recetas.service;

import com.example.dam.salesianos.gestion_recetas.dto.Edit.EditIngredientDto;
import com.example.dam.salesianos.gestion_recetas.dto.Getter.GetIngredientDetail;
import com.example.dam.salesianos.gestion_recetas.error.notFound.IngredientNotFoundException;
import com.example.dam.salesianos.gestion_recetas.repo.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public List<GetIngredientDetail> getAll() {
        return ingredientRepository.findAll().stream()
                .map(GetIngredientDetail::of)
                .toList();
    }

    public GetIngredientDetail getById(Long id){
        return ingredientRepository.findById(id)
                .map(GetIngredientDetail::of)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    public GetIngredientDetail addIngredient(EditIngredientDto request){
        return GetIngredientDetail.of(ingredientRepository.save(request.to()));
    }
}
