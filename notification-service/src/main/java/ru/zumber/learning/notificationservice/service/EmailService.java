package ru.zumber.learning.notificationservice.service;

import ru.zumber.learning.formessagedto.Operation;

public interface EmailService {

    void sendEmail(Operation operation, String email);
}
