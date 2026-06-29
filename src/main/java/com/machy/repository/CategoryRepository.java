package com.machy.repository;

import com.machy.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByActivoTrueOrderByNombre();
    boolean existsByNombre(String nombre);
}
