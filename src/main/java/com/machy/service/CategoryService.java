package com.machy.service;

import com.machy.entity.Category;
import com.machy.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findActive() {
        return categoryRepository.findByActivoTrueOrderByNombre();
    }

    public Category create(String nombre) {
        if (categoryRepository.existsByNombre(nombre)) {
            throw new RuntimeException("La categoría '" + nombre + "' ya existe");
        }
        return categoryRepository.save(Category.builder()
                .nombre(nombre).descripcion("").activo(true).build());
    }

    public void deactivate(UUID id) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        cat.setActivo(false);
        categoryRepository.save(cat);
    }
}
