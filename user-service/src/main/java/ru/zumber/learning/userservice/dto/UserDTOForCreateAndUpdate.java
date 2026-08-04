package ru.zumber.learning.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для создания или обновления пользователя")
public class UserDTOForCreateAndUpdate {
    @Schema(
            description = "Имя пользователя",
            example = "NameA",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;
    @Schema(
            description = "Email пользователя",
            example = "test@test.test",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;
    @Schema(
            description = "Возраст пользователя",
            example = "21",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer age;
}
