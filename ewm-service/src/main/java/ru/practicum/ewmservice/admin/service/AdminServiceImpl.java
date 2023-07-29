package ru.practicum.ewmservice.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.admin.controller.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.admin.controller.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.category.controller.dto.AddCategoryRequestDto;
import ru.practicum.ewmservice.category.controller.dto.CategoryDto;
import ru.practicum.ewmservice.category.dao.CategoryEntity;
import ru.practicum.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.ewmservice.category.service.CategoryService;
import ru.practicum.ewmservice.compilation.controller.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.controller.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dao.CompilationEntity;
import ru.practicum.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.ewmservice.compilation.repository.CompilationRepository;
import ru.practicum.ewmservice.compilation.service.CompilationService;
import ru.practicum.ewmservice.event.controller.dto.EventFullDto;
import ru.practicum.ewmservice.event.controller.dto.NewEventDto;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.service.EventService;
import ru.practicum.ewmservice.exception.BadRequestException;
import ru.practicum.ewmservice.exception.InvalidStateException;
import ru.practicum.ewmservice.location.service.LocationService;
import ru.practicum.ewmservice.state.ActionState;
import ru.practicum.ewmservice.state.State;
import ru.practicum.ewmservice.user.controller.dto.NewUserRequest;
import ru.practicum.ewmservice.user.controller.dto.UserDto;
import ru.practicum.ewmservice.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final LocationService locationService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final CompilationRepository compilationRepository;

    private final CategoryMapper categoryMapper;
    private final EventMapper eventMapper;
    private final CompilationMapper compilationMapper;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public CategoryDto createNewCategory(AddCategoryRequestDto newCategoryDto) {
        return categoryService.createNewCategory(newCategoryDto);
    }

    @Override
    public CategoryDto changeCategory(Long catId, AddCategoryRequestDto newCategoryDto) {
        return categoryService.changeCategory(catId, newCategoryDto);
    }

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        return userService.addNewUser(newUserRequest);
    }

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        return userService.getAllUserWithPagination(ids, from, size);
    }

    @Override
    public EventFullDto changeEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        EventEntity oldEvent = eventService.getById(eventId);
        if (oldEvent.getState() != State.PENDING) {
            throw new InvalidStateException("unable to change event status");
        }
        if (updateEventAdminRequest.getEventDate() != null && updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("unable to change event status, event already started");
        }
        if (oldEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new InvalidStateException("unable to change event, date should be 1 hour later");
        }
        EventEntity resultEvent = update(oldEvent, updateEventAdminRequest);
        resultEvent.setId(eventId);

        return eventService.saveChangeEventForAdmin(resultEvent);
    }

    @Override
    public List<EventFullDto> findEvents(List<Long> users, List<State> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, int from, int size) {

        LocalDateTime startFormat = null;
        LocalDateTime endFormat = null;

        if (rangeStart != null) {
            startFormat = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        }
        if (rangeEnd != null) {
            endFormat = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        }

        return eventService.findEventsWithParameters(users, states, categories, startFormat, endFormat, from, size);
    }

    @Override
    public CompilationDto createNewCompilation(NewCompilationDto newCompilationDto) {

        List<EventEntity> events = eventService.getEventListByEventIds(newCompilationDto.getEvents());
        CompilationEntity compilationEntity = compilationMapper.toCompilation(newCompilationDto, events);
        CompilationEntity compilationEntityResult = compilationRepository.save(compilationEntity);

        return compilationMapper.toCompilationDto(compilationEntityResult,
                eventService.getEventShortListWithSort(compilationEntityResult.getEvents(), false));
    }

    @Override
    public CompilationDto updateCompilation(Long id, UpdateCompilationRequest compilationRequest) {
        return compilationService.update(id, compilationRequest);
    }

    @Override
    public void deleteUser(Long userId) {
        userService.delete(userId);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryService.deleteCategory(id);
    }

    @Override
    public void deleteCompilation(Long id) {
        compilationService.delete(id);
    }

    private EventEntity update(EventEntity event, UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getCategory() != null) {
            CategoryEntity newCategory = categoryMapper.toEntity(categoryService.getById(updateEvent.getCategory()));
            event.setCategory(newCategory);
        }
        if (updateEvent.getLocation() != null) {
            locationService.delete(event.getLocation());
            event.setLocation(locationService.save(updateEvent.getLocation()));
        }
        if (updateEvent.getParticipantLimit() == null) {
            updateEvent.setParticipantLimit(event.getParticipantLimit());
        }
        if (updateEvent.getPaid() == null) {
            updateEvent.setPaid(event.isPaid());
        }
        if (updateEvent.getRequestModeration() == null) {
            updateEvent.setRequestModeration(event.isRequestModeration());
        }

        NewEventDto eventDto = eventMapper.toUpdateDto(updateEvent);
        EventEntity resultEvent = eventMapper.toEntity(event.getInitiator(), event.getCategory(),
                event.getLocation(), eventDto);

        if (resultEvent.getParticipantLimit() == null) resultEvent.setParticipantLimit(event.getParticipantLimit());
        if (resultEvent.getAnnotation() == null) resultEvent.setAnnotation(event.getAnnotation());
        if (resultEvent.getDescription() == null) resultEvent.setDescription(event.getDescription());
        if (resultEvent.getTitle() == null) resultEvent.setTitle(event.getTitle());
        if (resultEvent.getEventDate() == null) resultEvent.setEventDate(event.getEventDate());

        if (ActionState.PUBLISH_EVENT.equals(updateEvent.getStateAction())) {
            resultEvent.setState(State.PUBLISHED);
            resultEvent.setPublishedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        } else {
            resultEvent.setState(State.CANCELED);
        }
        return resultEvent;
    }
}
