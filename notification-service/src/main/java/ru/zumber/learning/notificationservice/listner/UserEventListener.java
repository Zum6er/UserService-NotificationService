package ru.zumber.learning.notificationservice.listner;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.zumber.learning.formessagedto.MessageDTO;
import ru.zumber.learning.notificationservice.service.EmailService;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(UserEventListener.class);

    @KafkaListener(topics = "user-events")
    public void listen(MessageDTO messageDTO) {
        logger.info("Получено сообщение: {}", messageDTO);
        emailService.sendEmail(messageDTO.operation(), messageDTO.email());
    }
}
