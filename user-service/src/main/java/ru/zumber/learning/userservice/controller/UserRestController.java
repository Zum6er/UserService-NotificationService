package ru.zumber.learning.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zumber.learning.userservice.dto.UserDTO;
import ru.zumber.learning.userservice.dto.UserDTOForCreateAndUpdate;
import ru.zumber.learning.userservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получение всех пользователей",
            description = "Получить список пользователей, сохраненных в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список всех пользователей получен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = UserDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            ref = "#/components/responses/500"
                    )
            })
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получаем пользователя",
            description = "Получаем пользователя из БД по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение пользователя",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            ref = "#/components/responses/404"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            ref = "#/components/responses/500"
                    )
            }
    )
    public UserDTO getUserById(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PostMapping
    @Operation(
            summary = "Сохранение пользователя",
            description = "Сохранение нового пользователя в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Успешное сохранение пользователя",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            ref = "#/components/responses/400"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            ref = "#/components/responses/409"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            ref = "#/components/responses/500"
                    )
            }
    )
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTOForCreateAndUpdate userDTOForCreateAndUpdate) {
        UserDTO savedUserDTO = userService.save(userDTOForCreateAndUpdate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedUserDTO);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление пользователя",
            description = "Обновление существующего пользователя в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное обновление пользователя",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            ref = "#/components/responses/400"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            ref = "#/components/responses/404"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            ref = "#/components/responses/409"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            ref = "#/components/responses/500"
                    )
            }
    )
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTOForCreateAndUpdate userDTOForCreateAndUpdate) {
        UserDTO updateUserDTO = userService.update(id, userDTOForCreateAndUpdate);
        return ResponseEntity.ok(updateUserDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление пользователя",
            description = "Удаление пользователя из БД по id",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Успешное удаление пользователя"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            ref = "#/components/responses/404"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            ref = "#/components/responses/500"
                    )
            }
    )
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
