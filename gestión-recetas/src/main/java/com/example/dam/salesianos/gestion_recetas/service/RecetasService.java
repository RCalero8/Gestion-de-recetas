package com.example.dam.salesianos.gestion_recetas.service;

import com.example.dam.salesianos.gestion_recetas.dto.CategoryResponse;
import com.example.dam.salesianos.gestion_recetas.dto.RecetaRequest;
import com.example.dam.salesianos.gestion_recetas.dto.RecetaResponse;
import com.example.dam.salesianos.gestion_recetas.error.TiempoInvalidoException;
import com.example.dam.salesianos.gestion_recetas.error.notFound.CategoryNotFoundException;
import com.example.dam.salesianos.gestion_recetas.error.notFound.RecetaNotFoundException;
import com.example.dam.salesianos.gestion_recetas.model.Category;
import com.example.dam.salesianos.gestion_recetas.model.Receta;
import com.example.dam.salesianos.gestion_recetas.repo.CategoryRepository;
import com.example.dam.salesianos.gestion_recetas.repo.RecetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecetasService {
    private final RecetaRepository recetaRepository;
    private final CategoryRepository categoryRepository;
    //Obtener todas
    public List<RecetaResponse> getAll() {
        return recetaRepository.findAll().stream()
                .map(RecetaResponse::of)
                .toList();
    }

    //Obtener lor id
    public RecetaResponse getRecetaById(Long id){
        return recetaRepository.findById(id)
                .map(RecetaResponse::of)
                .orElseThrow(() -> new RecetaNotFoundException(id));
    }

    public RecetaResponse addReceta(RecetaRequest recetaRequest) {
        validateReceta(recetaRequest);
        Category category = findCategoryById(recetaRequest.categoriaId());
        Receta receta = recetaRequest.toEntity(category);

        // DEBE SER recetaRepository.save(receta)
        return RecetaResponse.of(recetaRepository.save(receta));
    }

    public RecetaResponse updateReceta(Long id, RecetaRequest recetaRequest) {
        return recetaRepository.findById(id)
                .map(r -> {
                    validateReceta(recetaRequest);
                    Category category = findCategoryById(recetaRequest.categoriaId());

                    r.setNombre(recetaRequest.nombre());
                    r.setTiempoPreparacionMin(recetaRequest.tiempoPreparacionMin());
                    r.setDificultad(recetaRequest.dificultad());
                    r.setCategoria(category);

                    // DEBE SER recetaRepository.save(r)
                    return RecetaResponse.of(recetaRepository.save(r));
                })
                .orElseThrow(() -> new RecetaNotFoundException(id));
    }

    public void deleteReceta(Long id) {
        if (!recetaRepository.existsById(id)) {
            throw new RecetaNotFoundException(id);
        }
        recetaRepository.deleteById(id);
    }

    private void validateReceta(RecetaRequest recetaRequest) {
        if (recetaRequest.tiempoPreparacionMin() <= 0) {
            throw new TiempoInvalidoException(recetaRequest.tiempoPreparacionMin());
        }
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}
