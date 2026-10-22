package com.example.dam.salesianos.gestion_recetas.service;

import com.example.dam.salesianos.gestion_recetas.dto.Edit.EditCategoryDto;
import com.example.dam.salesianos.gestion_recetas.dto.Getter.GetCategoryDetail;
import com.example.dam.salesianos.gestion_recetas.error.notFound.CategoryNotFoundException;
import com.example.dam.salesianos.gestion_recetas.model.Category;
import com.example.dam.salesianos.gestion_recetas.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<GetCategoryDetail> getAll() {
        return categoryRepository.findAll().stream()
                .map(GetCategoryDetail::of)
                .toList();
    }

    public GetCategoryDetail getById(Long id) {
        return GetCategoryDetail.of(findOrThrow(id));
    }

    public GetCategoryDetail addCategory(EditCategoryDto request) {
        Category saved = categoryRepository.save(request.to());
        return GetCategoryDetail.of(saved);
    }

    public GetCategoryDetail updateCategory(Long id, EditCategoryDto request) {
        Category category = findOrThrow(id);
        category.setNombre(request.nombre());
        category.setDescripcion(request.descripcion());
        return GetCategoryDetail.of(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        categoryRepository.delete(findOrThrow(id));
    }

    private Category findOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
