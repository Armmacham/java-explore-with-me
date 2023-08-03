package ru.practicum.ewmservice.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.ewmservice.compilation.dao.CompilationEntity;


public interface CompilationRepository extends CrudRepository<CompilationEntity, Long> {

    Page<CompilationEntity> getCompilationsByPinned(boolean pinned, Pageable pageable);

}
