package ru.zumber.learning.userservice.controller;

import ru.zumber.learning.userservice.exception.NoCorrectUser;
import ru.zumber.learning.userservice.exception.UserNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BadControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(BadControllerAdvice.class);

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ProblemDetail> handleUserNotFound(UserNotFound userNotFound){
        logger.warn(userNotFound.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, userNotFound.getMessage()));
    }

    @ExceptionHandler(NoCorrectUser.class)
    public ResponseEntity<ProblemDetail> handleNoCorrectUser(NoCorrectUser noCorrectUser){
        logger.warn(noCorrectUser.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, noCorrectUser.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception exception){
        logger.error("Непредвиденная ошибка ", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));
    }
}
