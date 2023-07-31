package ru.practicum.ewmservice.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.ewmservice.category.dao.CategoryEntity;


public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
    Page<CategoryEntity> findAll(Pageable pageable);
}
