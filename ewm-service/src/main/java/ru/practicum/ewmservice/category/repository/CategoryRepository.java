package ru.practicum.ewmservice.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.category.dao.CategoryEntity;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {

    @Query("SELECT c FROM CategoryEntity c")
    Page<CategoryEntity> getAll(Pageable pageable);

}
