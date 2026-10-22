package com.example.dam.salesianos.gestion_recetas.service;

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
public class RecetaService {
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
        return RecetaResponse.of(findRecetaById(id));
    }

    //Añadir Receta
    public RecetaResponse addReceta(RecetaRequest recetaRequest) {
        validateReceta(recetaRequest);
        Category category = findCategoryById(recetaRequest.categoriaId());
        Receta receta = recetaRequest.toEntity(category);
        return RecetaResponse.of(recetaRepository.save(receta));
    }

    //Actualizar Receta
    public RecetaResponse updateReceta(Long id, RecetaRequest recetaRequest) {
        Receta receta = findRecetaById(id);
        validateReceta(recetaRequest);
        Category categoria = findCategoryById(recetaRequest.categoriaId());

        receta.setNombre(recetaRequest.nombre());
        receta.setTiempoPreparacionMin(recetaRequest.tiempoPreparacionMin());
        receta.setDificultad(recetaRequest.dificultad());
        receta.setCategoria(categoria);

        return RecetaResponse.of(recetaRepository.save(receta));
    }

    //Borrar Receta
    public void deleteReceta(Long id) {
       Receta receta = findRecetaById(id);
       recetaRepository.delete(receta);
    }

    //Validar Receta
    private void validateReceta(RecetaRequest recetaRequest) {
        if (recetaRequest.tiempoPreparacionMin() != null && recetaRequest.tiempoPreparacionMin() <= 0) {
            throw new TiempoInvalidoException(recetaRequest.tiempoPreparacionMin());
        }
    }

    //Encontrar Categoria
    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    private Receta findRecetaById(Long id) {
        return recetaRepository.findById(id)
                .orElseThrow(() -> new RecetaNotFoundException(id));
    }
}
