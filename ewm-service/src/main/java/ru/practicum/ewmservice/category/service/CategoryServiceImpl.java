package ru.practicum.ewmservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.category.dao.CategoryEntity;
import ru.practicum.ewmservice.category.controller.dto.AddCategoryRequestDto;
import ru.practicum.ewmservice.category.controller.dto.CategoryDto;
import ru.practicum.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.ewmservice.category.repository.CategoryRepository;
import ru.practicum.ewmservice.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createNewCategory(AddCategoryRequestDto addCategoryRequestDto) {
        log.info("Add new category {}", addCategoryRequestDto);
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(addCategoryRequestDto)));
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        log.info("get categories from {} size {}", from, size);
        return categoryRepository.getAll(PageRequest.of(from / size, size))
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        log.info("get category by id {}", id);
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Category with id=" + id + " not found"));
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("delete category {}", id);
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id=" + id + " not found"));
        categoryRepository.delete(categoryEntity);
    }

    @Override
    public CategoryDto changeCategory(Long id, AddCategoryRequestDto addCategoryRequestDto) {
        log.info("Change category with id {}", id);
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id=" + id + " not found"));
        categoryEntity.setName(addCategoryRequestDto.getName());
        CategoryEntity saved = categoryRepository.save(categoryEntity);
        return categoryMapper.toDto(saved);
    }
}

