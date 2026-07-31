package ru.zumber.learning.formessagedto;

import lombok.Builder;

@Builder
public record MessageDTO(
        Operation operation,
        String email
) {
}
