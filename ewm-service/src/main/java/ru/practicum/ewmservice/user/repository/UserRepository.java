package ru.practicum.ewmservice.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.user.dao.UserEntity;

import java.util.List;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAllByIdIn(List<Long> ids, Pageable pageable);

}
