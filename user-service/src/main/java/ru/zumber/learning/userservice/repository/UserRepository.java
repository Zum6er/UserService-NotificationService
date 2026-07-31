package ru.zumber.learning.userservice.repository;

import ru.zumber.learning.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
