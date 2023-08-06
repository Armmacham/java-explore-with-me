package ru.practicum.ewmservice.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.exception.EntityNotFoundException;
import ru.practicum.ewmservice.user.controller.dto.NewUserRequest;
import ru.practicum.ewmservice.user.controller.dto.UserDto;
import ru.practicum.ewmservice.user.dao.UserEntity;
import ru.practicum.ewmservice.user.mapper.UserMapper;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto addNewUser(NewUserRequest newUserRequest) {
        log.info("add new user {}", newUserRequest);
        return userMapper.toDto(userRepository.save(userMapper.toEntity(newUserRequest)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUserWithPagination(List<Long> ids, int from, int size) {
        log.info("get all users where id in {} from {} size {}", ids, from, size);
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(PageRequest.of(from / size, size))
                    .stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size))
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public UserEntity getById(Long id) {
        log.info("find user by id {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with id=" + id + " not found"));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with id=" + id + " not found"));
        userRepository.delete(user);
        log.info("deleted user {}", id);
    }
}
