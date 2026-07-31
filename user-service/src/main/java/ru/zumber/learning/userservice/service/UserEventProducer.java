package ru.zumber.learning.userservice.service;

import ru.zumber.learning.formessagedto.MessageDTO;

public interface UserEventProducer {

    void sendMessage(MessageDTO messageDTO);
}
