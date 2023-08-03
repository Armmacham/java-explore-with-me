package ru.practicum.ewmservice.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.user.controller.dto.NewUserRequest;
import ru.practicum.ewmservice.user.controller.dto.UserDto;
import ru.practicum.ewmservice.user.controller.dto.UserShortDto;
import ru.practicum.ewmservice.user.dao.UserEntity;

@Component
public class UserMapper {

    public UserEntity toEntity(NewUserRequest newUserRequest) {
        UserEntity user = new UserEntity();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());
        return user;
    }

    public UserDto toDto(UserEntity user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public UserShortDto toShortDto(UserEntity user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
