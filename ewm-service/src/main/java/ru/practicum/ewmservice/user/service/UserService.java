package ru.practicum.ewmservice.user.service;


import ru.practicum.ewmservice.user.controller.dto.NewUserRequest;
import ru.practicum.ewmservice.user.controller.dto.UserDto;
import ru.practicum.ewmservice.user.dao.UserEntity;

import java.util.List;

public interface UserService {

    UserDto addNewUser(NewUserRequest newUserRequest);


    List<UserDto> getAllUserWithPagination(List<Long> ids, int from, int size);

    UserEntity getById(Long id);

    void delete(Long id);
}
