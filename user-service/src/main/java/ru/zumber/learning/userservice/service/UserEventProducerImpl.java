package ru.zumber.learning.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.zumber.learning.formessagedto.MessageDTO;

@Service
@RequiredArgsConstructor
public class UserEventProducerImpl implements UserEventProducer {
    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;

    @Override
    public void sendMessage(MessageDTO messageDTO) {
        kafkaTemplate.send("user-events", messageDTO);
    }
}
