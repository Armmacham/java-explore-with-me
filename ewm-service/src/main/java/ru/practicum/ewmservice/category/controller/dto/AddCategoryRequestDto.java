package ru.practicum.ewmservice.category.controller.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AddCategoryRequestDto {
    @NotBlank
    @Size(max = 50)
    String name;
}
