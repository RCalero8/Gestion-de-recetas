package com.example.dam.salesianos.gestion_recetas.service;

import com.example.dam.salesianos.gestion_recetas.dto.Edit.EditRecetaDto;
import com.example.dam.salesianos.gestion_recetas.dto.Edit.EditRecetaIngredienteDto;
import com.example.dam.salesianos.gestion_recetas.dto.Getter.GetRecetaDetail;
import com.example.dam.salesianos.gestion_recetas.model.Category;
import com.example.dam.salesianos.gestion_recetas.model.Ingredient;
import com.example.dam.salesianos.gestion_recetas.model.Receta;
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

   private final RecetaRepositoy recetaRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;

    public List<GetRecetaDetail> getAll() {
        return recetaRepository.findAll().stream()
                .map(GetRecetaDetail::of)
                .toList();
    }

    public GetRecetaDetail getRecetaById(Long id) {
        return GetRecetaDetail.of(findRecetaOrThrow(id));
    }

    @Transactional
    public GetRecetaDetail addReceta(EditRecetaDto request) {
        validarTiempo(request.tiempoPreparacionMin());
        Category categoria = findCategoryOrThrow(request.categoriaId());
        Receta guardada = recetaRepository.save(request.to(categoria));
        return GetRecetaDetail.of(guardada);
    }

    @Transactional
    public GetRecetaDetail updateReceta(Long id, EditRecetaDto request) {
        Receta receta = findRecetaOrThrow(id);
        validarTiempo(request.tiempoPreparacionMin());
        Category categoria = findCategoryOrThrow(request.categoriaId());

        receta.setNombre(request.nombre());
        receta.setTiempoPreparacionMin(request.tiempoPreparacionMin());
        receta.setDificultad(request.dificultad());
        receta.setCategoria(categoria);
        // No se tocan los ingredientes

        return GetRecetaDetail.of(recetaRepository.save(receta));
    }

    @Transactional
    public void deleteReceta(Long id) {
        // Borra los datos
        recetaRepository.delete(findRecetaOrThrow(id));
    }

    /** Relación M:M: añade un ingrediente existente a una receta existente. */
    @Transactional
    public GetRecetaDetail addIngrediente(Long recetaId, EditRecetaIngredienteDto request) {
        Receta receta = findRecetaOrThrow(recetaId);
        Ingredient ingrediente = ingredientRepository.findById(request.ingredienteId())
                .orElseThrow(() -> new IngredientNotFoundException(request.ingredienteId()));

        boolean yaAnadido = receta.getIngredientes().stream()
                .anyMatch(ri -> ri.getIngrediente().getId().equals(ingrediente.getId()));
        if (yaAnadido) {
            throw new IngredienteYaAnadidoException(receta.getNombre(), ingrediente.getNombre());
        }

        // cascade = ALL en Receta.ingredientes: la fila intermedia se inserta al confirmar la transacción
        receta.getIngredientes().add(request.to(receta, ingrediente));
        return GetRecetaDetail.of(receta);
    }

    private void validarTiempo(Integer tiempoPreparacionMin) {
        if (tiempoPreparacionMin <= 0) {
            throw new TiempoInvalidoException(tiempoPreparacionMin);
        }
    }

    private Receta findRecetaOrThrow(Long id) {
        return recetaRepository.findById(id)
                .orElseThrow(() -> new RecetaNotFoundException(id));
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
