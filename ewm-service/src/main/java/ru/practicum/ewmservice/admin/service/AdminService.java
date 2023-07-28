package ru.practicum.ewmservice.admin.service;

import ru.practicum.ewmservice.location.dao.controller.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.location.dao.controller.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.category.controller.dto.AddCategoryRequestDto;
import ru.practicum.ewmservice.category.controller.dto.CategoryDto;
import ru.practicum.ewmservice.compilation.controller.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.controller.dto.NewCompilationDto;
import ru.practicum.ewmservice.event.controller.dto.EventFullDto;
import ru.practicum.ewmservice.user.controller.dto.NewUserRequest;
import ru.practicum.ewmservice.state.State;
import ru.practicum.ewmservice.user.controller.dto.UserDto;

import java.util.List;

public interface AdminService {

    CategoryDto createNewCategory(AddCategoryRequestDto newCategoryDto);

    CategoryDto changeCategory(Long id, AddCategoryRequestDto newCategoryDto);

    UserDto createUser(NewUserRequest newUserRequest);

    List<UserDto> getAllUsers(List<Long> ids, int from, int size);

    EventFullDto changeEvent(Long id, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> findEvents(List<Long> users, List<State> states, List<Long> categories,
                                        String rangeStart, String rangeEnd, int from, int size);

    CompilationDto createNewCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(Long id, UpdateCompilationRequest compilationRequest);

    void deleteUser(Long userId);

    void deleteCategory(Long id);

    void deleteCompilation(Long id);

}
