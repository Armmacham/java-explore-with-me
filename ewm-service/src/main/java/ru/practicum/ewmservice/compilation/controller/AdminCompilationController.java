package ru.practicum.ewmservice.compilation.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmservice.compilation.controller.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.controller.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.controller.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PatchMapping("/compilations/{id}")
    public CompilationDto updateCompilation(@PathVariable Long id,
                                            @RequestBody @Valid UpdateCompilationRequest compilationRequest) {
        return compilationService.update(id, compilationRequest);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.delete(compId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createNewCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.createNewCompilation(newCompilationDto);
    }
}
