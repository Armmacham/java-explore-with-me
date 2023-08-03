package ru.practicum.ewmservice.compilation.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmservice.compilation.controller.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.service.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/compilations")
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping()
    public List<CompilationDto> getCompilation(
            @RequestParam(value = "pinned", defaultValue = "false") boolean pinned,
            @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
            @Min(1) @RequestParam(value = "size", defaultValue = "10") int size) {
        return compilationService.getCompilation(pinned, from, size);
    }

    @GetMapping("/{id}")
    public CompilationDto getCompilationById(@PathVariable Long id) {
        return compilationService.getCompilationById(id);
    }
}
