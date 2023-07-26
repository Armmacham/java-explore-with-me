package ru.practicum.ewmservice.compilation.service;


import ru.practicum.ewmservice.admin.controller.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.compilation.controller.dto.CompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto getCompilationById(Long id);

    List<CompilationDto> getCompilation(boolean pinned, int from, int size);

    CompilationDto update(Long compId, UpdateCompilationRequest compilationRequest);

    void delete(Long id);
}
