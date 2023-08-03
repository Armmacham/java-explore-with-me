package ru.practicum.ewmservice.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.category.dao.CategoryEntity;
import ru.practicum.ewmservice.category.controller.dto.CategoryDto;
import ru.practicum.ewmservice.category.controller.dto.AddCategoryRequestDto;

@Component
public class CategoryMapper {

    public CategoryDto toDto(CategoryEntity categoryEntity) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryEntity.getId());
        categoryDto.setName(categoryEntity.getName());
        return categoryDto;
    }

    public CategoryEntity toEntity(AddCategoryRequestDto addCategoryRequestDto) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(addCategoryRequestDto.getName());
        return categoryEntity;
    }

    public CategoryEntity toEntity(CategoryDto categoryDto) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryDto.getId());
        categoryEntity.setName(categoryDto.getName());
        return categoryEntity;
    }
}
