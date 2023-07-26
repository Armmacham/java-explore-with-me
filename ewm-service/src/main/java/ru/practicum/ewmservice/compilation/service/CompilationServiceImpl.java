package ru.practicum.ewmservice.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.admin.controller.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.compilation.controller.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dao.CompilationEntity;
import ru.practicum.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.ewmservice.compilation.repository.CompilationRepository;
import ru.practicum.ewmservice.event.service.EventService;
import ru.practicum.ewmservice.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto getCompilationById(Long id) {

        CompilationEntity compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("compilation " + id + " not found"));

        return compilationMapper.toCompilationDto(compilation,
                eventService.getEventShortListWithSort(compilation.getEvents(), false));
    }

    @Override
    public List<CompilationDto> getCompilation(boolean pinned, int from, int size) {
        return compilationRepository
                .getCompilationsByPinned(pinned, PageRequest.of(from / size, size))
                .stream()
                .map(compilation ->
                        compilationMapper
                                .toCompilationDto(
                                        compilation,
                                        eventService.getEventShortListWithSort(compilation.getEvents(), false)))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto update(Long id, UpdateCompilationRequest compilationRequest) {
        CompilationEntity compilation = compilationRepository.findById(id).orElse(null);
        if (compilation == null) {
            log.info("Подборки с id {} не найдено", id);
            throw new EntityNotFoundException("compilation with id=" + id + " not found");
        }
        if (compilationRequest.getEvents().size() != 0) {
            compilation.setEvents(eventService.getEventListByEventIds(compilationRequest.getEvents()));
        }
        if (compilationRequest.getTitle() != null) {
            compilation.setTitle(compilationRequest.getTitle());
        }
        if (compilationRequest.getPinned() != null) {
            compilation.setPinned(compilationRequest.getPinned());
        }
        CompilationEntity compilationResult = compilationRepository.save(compilation);

        return compilationMapper.toCompilationDto(compilationResult,
                eventService.getEventShortListWithSort(compilationResult.getEvents(), false));
    }

    @Override
    public void delete(Long id) {
        CompilationEntity compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("compilation with id=" + id + " not found"));
        compilationRepository.delete(compilation);
    }
}
