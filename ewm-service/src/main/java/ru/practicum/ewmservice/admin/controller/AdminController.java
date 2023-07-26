package ru.practicum.ewmservice.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmservice.admin.controller.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.admin.controller.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.admin.service.AdminService;
import ru.practicum.ewmservice.category.controller.dto.AddCategoryRequestDto;
import ru.practicum.ewmservice.category.controller.dto.CategoryDto;
import ru.practicum.ewmservice.compilation.controller.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.controller.dto.NewCompilationDto;
import ru.practicum.ewmservice.event.controller.dto.EventFullDto;
import ru.practicum.ewmservice.state.State;
import ru.practicum.ewmservice.user.controller.dto.NewUserRequest;
import ru.practicum.ewmservice.user.controller.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/categories")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto createNewCategory(@RequestBody @Valid AddCategoryRequestDto addCategoryRequestDto) {
        return adminService.createNewCategory(addCategoryRequestDto);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
    }

    @PatchMapping("/categories/{id}")
    public CategoryDto changeCategory(@PathVariable Long id,
                                      @RequestBody @Valid AddCategoryRequestDto addCategoryRequestDto) {
        return adminService.changeCategory(id, addCategoryRequestDto);
    }

    @GetMapping("/events")
    public Collection<EventFullDto> getAllEvents(
            @RequestParam(value = "users", required = false) List<Long> users,
            @RequestParam(value = "states", required = false) List<State> states,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @Min(0) @RequestParam(value = "from", defaultValue = "0", required = false) int from,
            @Min(1) @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        return adminService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto changeEvent(@PathVariable Long eventId,
                                    @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {

        return adminService.changeEvent(eventId, updateEventAdminRequest);
    }

    @PatchMapping("/compilations/{id}")
    public CompilationDto updateCompilation(@PathVariable Long id,
                                            @RequestBody UpdateCompilationRequest compilationRequest) {
        return adminService.updateCompilation(id, compilationRequest);
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers(
            @RequestParam(value = "ids") List<Long> ids,
            @Min(0) @RequestParam(value = "from", required = false, defaultValue = "0") int from,
            @Min(1) @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return adminService.getAllUsers(ids, from, size);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        adminService.deleteCompilation(compId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createNewCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return adminService.createNewCompilation(newCompilationDto);
    }

    @PostMapping("/users")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto createNewUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return adminService.createUser(newUserRequest);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
    }
}
