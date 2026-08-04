package ru.zumber.learning.userservice.exception;

public class UserEmailDuplicated extends RuntimeException {
    public UserEmailDuplicated(String message) {
        super(message);
    }
}
