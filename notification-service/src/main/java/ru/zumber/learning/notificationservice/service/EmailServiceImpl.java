package ru.zumber.learning.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.zumber.learning.formessagedto.Operation;
import ru.zumber.learning.notificationservice.exception.MailSendException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendEmail(Operation operation, String email) {
        SimpleMailMessage message = createMessage(operation, email);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            logger.error("Отправить письмо пользователю {}, не удалось", email, e);
            throw new MailSendException("Ошибка отправки письма "+ email, e);
        }
    }

    private SimpleMailMessage createMessage(Operation operation, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        switch (operation) {
            case CREATE -> {
                message.setSubject("Ваш аккаунт был создан");
                message.setText("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.");
            }
            case DELETE -> {
                message.setSubject("Ваш аккаунт был удалён");
                message.setText("Здравствуйте! Ваш аккаунт был удалён.");
            }
        }
        return message;
    }
}