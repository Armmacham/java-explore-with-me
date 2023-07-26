package ru.practicum.ewmservice.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.compilation.dao.CompilationEntity;

@Repository
public interface CompilationRepository extends CrudRepository<CompilationEntity, Long> {

    Page<CompilationEntity> getCompilationsByPinned(boolean pinned, Pageable pageable);

}
