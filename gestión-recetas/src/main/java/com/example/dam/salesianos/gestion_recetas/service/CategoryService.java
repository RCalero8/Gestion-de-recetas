package com.example.dam.salesianos.gestion_recetas.service;

import com.example.dam.salesianos.gestion_recetas.dtos.CategoryRequest;
import com.example.dam.salesianos.gestion_recetas.dtos.CategoryResponse;
import com.example.dam.salesianos.gestion_recetas.error.notFound.CategoryNotFoundException;
import com.example.dam.salesianos.gestion_recetas.model.Category;
import com.example.dam.salesianos.gestion_recetas.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository category;

    //Obtener todos
    public List<CategoryResponse> getAll() {
        return category.findAll().stream()
                .map(CategoryResponse::of)
                .toList();
    }

    //Obtener por Id
    public CategoryResponse getById(Long id) {
        return CategoryResponse.of(findOrThrow(id));
    }

    //Añadir categoria
    public CategoryResponse addCategory(CategoryRequest request) {
        Category saved = category.save(request.toEntity());
        return CategoryResponse.of(saved);
    }

    //Actualizar Categoria
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category categoria = findOrThrow(id);
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        return CategoryResponse.of(category.save(categoria));
    }

    public void deleteCategory(Long id) {
        category.delete(findOrThrow(id));
    }

    private Category findOrThrow(Long id) {
        return category.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

}
