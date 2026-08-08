package ru.zumber.learning.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zumber.learning.userservice.assembler.UserModelAssembler;
import ru.zumber.learning.userservice.dto.UserDTO;
import ru.zumber.learning.userservice.dto.UserDTOForCreateAndUpdate;
import ru.zumber.learning.userservice.service.UserService;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final UserModelAssembler userAssembler;

    @GetMapping
    @Operation(summary = "Получение всех пользователей",
            description = "Получить список пользователей, сохраненных в БД",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Список всех пользователей получен",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = CollectionModel.class))
                            )
                    ),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/500")
            })
    public CollectionModel<EntityModel<UserDTO>> getAllUsers() {
        List<EntityModel<UserDTO>> entityUsers = userService.findAll()
                .stream()
                .map(userAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<UserDTO>> collectionModel = CollectionModel.of(entityUsers);
        collectionModel.add(linkTo(methodOn(UserRestController.class).getAllUsers()).withSelfRel());
        return collectionModel;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получаем пользователя",
            description = "Получаем пользователя из БД по его id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Успешное получение пользователя",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = EntityModel.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/404"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/500")
            }
    )
    public EntityModel<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO userDTO = userService.getUser(id);
        return userAssembler.toModel(userDTO);
    }

    @PostMapping
    @Operation(summary = "Сохранение пользователя",
            description = "Сохранение нового пользователя в БД",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Успешное сохранение пользователя",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = EntityModel.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/400"),
                    @ApiResponse(responseCode = "409", ref = "#/components/responses/409"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/500")
            }
    )
    public ResponseEntity<EntityModel<UserDTO>> createUser(@RequestBody UserDTOForCreateAndUpdate userDTOForCreateAndUpdate) {
        UserDTO savedUserDTO = userService.save(userDTOForCreateAndUpdate);
        EntityModel<UserDTO> entityModel = userAssembler.toModel(savedUserDTO);
        URI location = linkTo(methodOn(UserRestController.class).getUserById(savedUserDTO.getId())).toUri();
        return ResponseEntity.created(location)
                .body(entityModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление пользователя",
            description = "Обновление существующего пользователя в БД",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Успешное обновление пользователя",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = EntityModel.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/400"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/404"),
                    @ApiResponse(responseCode = "409", ref = "#/components/responses/409"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/500")
            }
    )
    public ResponseEntity<EntityModel<UserDTO>> updateUser(@PathVariable Integer id, @RequestBody UserDTOForCreateAndUpdate userDTOForCreateAndUpdate) {
        UserDTO updateUserDTO = userService.update(id, userDTOForCreateAndUpdate);
        EntityModel<UserDTO> entityModel = userAssembler.toModel(updateUserDTO);
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя",
            description = "Удаление пользователя из БД по id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Успешное удаление пользователя"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/404"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/500")
            }
    )
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
