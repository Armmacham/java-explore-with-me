package ru.practicum.ewmservice.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.compilation.controller.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.controller.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.controller.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.compilation.dao.CompilationEntity;
import ru.practicum.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.ewmservice.compilation.repository.CompilationRepository;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.event.service.EventService;
import ru.practicum.ewmservice.exception.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    private final CompilationMapper compilationMapper;

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long id) {

        CompilationEntity compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("compilation " + id + " not found"));

        return compilationMapper.toCompilationDto(compilation,
                eventService.getEventShortListWithSort(compilation.getEvents(), false));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilation(boolean pinned, int from, int size) {
        List<CompilationEntity> compilations = compilationRepository
                .getCompilationsByPinned(pinned, PageRequest.of(from / size, size)).stream().collect(Collectors.toList());

        Map<Long, Long> views = eventService.getViews(
                compilations.stream()
                        .flatMap(e -> e.getEvents().stream())
                        .collect(Collectors.toList()),
                compilations.stream()
                        .flatMap(e -> e.getEvents().stream())
                        .map(e -> "/events/" + e.getId()).collect(Collectors.toList()));

        Map<Long, Long> eventIdConfirmedCount = eventService.findEventIdConfirmedCount(
                compilations.stream()
                        .flatMap(e -> e.getEvents().stream())
                        .map(EventEntity::getId)
                        .collect(Collectors.toList()));

        return compilations
                .stream()
                .map(compilation ->
                        compilationMapper
                                .toCompilationDto(
                                        compilation,
                                        eventService.getEventShortListWithSort(compilation.getEvents(), false, views, eventIdConfirmedCount)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompilationDto update(Long id, UpdateCompilationRequest compilationRequest) {
        CompilationEntity compilation = compilationRepository.findById(id).orElse(null);
        if (compilation == null) {
            log.info("Подборки с id {} не найдено", id);
            throw new EntityNotFoundException("compilation with id=" + id + " not found");
        }
        if (compilationRequest.getEvents() != null && !compilationRequest.getEvents().isEmpty()) {
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
    @Transactional
    public void delete(Long id) {
        CompilationEntity compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("compilation with id=" + id + " not found"));
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public CompilationDto createNewCompilation(NewCompilationDto newCompilationDto) {
        List<EventEntity> events = eventService.getEventListByEventIds(newCompilationDto.getEvents());
        CompilationEntity compilationEntity = compilationMapper.toCompilation(newCompilationDto, events);
        CompilationEntity compilationEntityResult = compilationRepository.save(compilationEntity);

        return compilationMapper.toCompilationDto(compilationEntityResult,
                eventService.getEventShortListWithSort(compilationEntityResult.getEvents(), false));
    }
}
