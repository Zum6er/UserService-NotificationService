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
        }
    }

    private SimpleMailMessage createMessage(Operation operation, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        switch (operation) {
            case CREATE -> {
                message.setSubject("Сохранение пользователя");
                message.setText("""
                        Здравствуйте!
                        Пользователь был успешно сохранен в БД
                        """);
            }
            case DELETE -> {
                message.setSubject("Удаление пользователя");
                message.setText("""
                        Здравствуйте!
                        Пользователь был удален
                        """);
            }
        }
        return message;
    }
}