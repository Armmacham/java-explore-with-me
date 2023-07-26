package ru.practicum.ewmservice.category.service;

import ru.practicum.ewmservice.category.controller.dto.AddCategoryRequestDto;
import ru.practicum.ewmservice.category.controller.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createNewCategory(AddCategoryRequestDto addCategoryRequestDto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getById(Long catId);

    void deleteCategory(Long catId);

    CategoryDto changeCategory(Long catId, AddCategoryRequestDto addCategoryRequestDto);
}
