package ru.practicum.ewmservice.compilation.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private boolean pinned;
    @NotBlank
    @Size(max = 50)
    private String title;
}
