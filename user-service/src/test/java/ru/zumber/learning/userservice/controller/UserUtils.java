package ru.zumber.learning.userservice.controller;

import ru.zumber.learning.userservice.entity.User;

import java.time.LocalDate;

public class UserUtils {

    public static User getUserA() {
        return User.builder()
                .name("NameA")
                .email("emailA@test.test")
                .age(34)
                .createdAt(LocalDate.now())
                .build();
    }

    public static User getUserB() {
        return User.builder()
                .name("NameB")
                .email("emailB@test.test")
                .age(45)
                .createdAt(LocalDate.now())
                .build();
    }

    public static User getUserC() {
        return User.builder()
                .name("NameC")
                .email("emailC@test.test")
                .age(56)
                .createdAt(LocalDate.now())
                .build();
    }
}
