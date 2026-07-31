package ru.zumber.learning.userservice.exception;

public class NoCorrectUser extends RuntimeException {
    public NoCorrectUser(String message) {
        super(message);
    }
}
