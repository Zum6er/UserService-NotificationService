package ru.zumber.learning.notificationservice.dto;

import lombok.Builder;
import ru.zumber.learning.formessagedto.Operation;

@Builder
public record CreateMessageDTO(
    Operation operation,
    String email){
}
