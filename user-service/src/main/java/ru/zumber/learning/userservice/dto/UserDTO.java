package ru.zumber.learning.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для представления данных пользователя")
public class UserDTO {
    @Schema(
            description = "Уникальный идентификатор",
            example = "11",
            accessMode =  Schema.AccessMode.READ_ONLY
    )
    private Integer id;
    @Schema(
            description = "Имя пользователя",
            example = "NameA"
    )
    private String name;
    @Schema(
            description = "Email пользователя",
            example = "test@test.test"
    )
    private String email;
    @Schema(
            description = "Возраст пользователя",
            example = "34"
    )
    private Integer age;
    @Schema(
            description = "Дата создания",
            example = "2026-08-06",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDate createdAt;
}
