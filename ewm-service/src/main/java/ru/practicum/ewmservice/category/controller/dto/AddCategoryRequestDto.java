package ru.practicum.ewmservice.category.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddCategoryRequestDto {
    @NotBlank
    String name;
}
