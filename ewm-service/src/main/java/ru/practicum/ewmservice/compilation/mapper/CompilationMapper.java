package ru.practicum.ewmservice.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.compilation.controller.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.controller.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dao.CompilationEntity;
import ru.practicum.ewmservice.event.controller.dto.EventShortDto;
import ru.practicum.ewmservice.event.dao.EventEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {

    public CompilationEntity toCompilation(NewCompilationDto compilationDto, List<EventEntity> events) {
        CompilationEntity compilationEntity = new CompilationEntity();
        compilationEntity.setEvents(events);
        compilationEntity.setTitle(compilationDto.getTitle());
        compilationEntity.setPinned(compilationDto.isPinned());
        return compilationEntity;
    }

    public CompilationDto toCompilationDto(CompilationEntity compilationEntity, List<EventShortDto> eventShorts) {
        return new CompilationDto(
                compilationEntity.getId(),
                eventShorts.stream().sorted(Comparator.comparing(EventShortDto::getViews)
                        .reversed()).collect(Collectors.toList()),
                compilationEntity.isPinned(),
                compilationEntity.getTitle()
        );
    }
}
