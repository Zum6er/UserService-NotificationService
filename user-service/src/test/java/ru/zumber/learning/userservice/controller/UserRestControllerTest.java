package ru.zumber.learning.userservice.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;
import ru.zumber.learning.userservice.assembler.UserModelAssembler;
import ru.zumber.learning.userservice.dto.UserDTO;
import ru.zumber.learning.userservice.dto.UserDTOForCreateAndUpdate;
import ru.zumber.learning.userservice.exception.NoCorrectUser;
import ru.zumber.learning.userservice.exception.UserNotFound;
import ru.zumber.learning.userservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
@ActiveProfiles("test")
class UserRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @MockitoBean
    UserModelAssembler userAssembler;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Получить список всех пользователей")
    void getAllUsers() throws Exception {
        //Given
        UserDTO userDTO = UserDTO.builder()
                .id(1).name("TestNameA").email("TestEmailA@test.test").age(12).build();
        UserDTO userDTO2 = UserDTO.builder()
                .id(2).name("TestNameB").email("TestEmailB@test.test").age(23).build();
        UserDTO userDTO3 = UserDTO.builder()
                .id(3).name("TestNameC").email("TestEmailC@test.test").age(34).build();
        List<UserDTO> userDTOList = List.of(userDTO, userDTO2, userDTO3);
        EntityModel<UserDTO> entityModel = EntityModel.of(userDTO);
        EntityModel<UserDTO> entityModel2 = EntityModel.of(userDTO2);
        EntityModel<UserDTO> entityModel3 = EntityModel.of(userDTO3);
        given(userService.findAll()).willReturn(userDTOList);
        given(userAssembler.toModel(userDTO)).willReturn(entityModel);
        given(userAssembler.toModel(userDTO2)).willReturn(entityModel2);
        given(userAssembler.toModel(userDTO3)).willReturn(entityModel3);
        //When
        mockMvc.perform(get("/user"))
                //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userDTOList").isArray())
                .andExpect(jsonPath("$._embedded.userDTOList.length()").value(3))
                .andExpect(jsonPath("$._embedded.userDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.userDTOList[1].name").value("TestNameB"))
                .andExpect(jsonPath("$._embedded.userDTOList[2].age").value(34));
        verify(userService).findAll();
        verify(userAssembler).toModel(userDTO);
        verify(userAssembler).toModel(userDTO2);
        verify(userAssembler).toModel(userDTO3);
        verifyNoMoreInteractions(userService, userAssembler);
    }

    @Test
    @DisplayName("Получить пустой список пользователей")
    void getEmptyAllUsers() throws Exception {
        //Given
        given(userService.findAll()).willReturn(Collections.emptyList());
        //When
        mockMvc.perform(get("/user"))
                //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist());
        verify(userService).findAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Получить существующего пользователя по Id")
    void getUserById() throws Exception {
        //Given
        UserDTO userDTO = UserDTO.builder()
                .id(1)
                .name("TestName")
                .email("TestEmail@test.test")
                .age(34)
                .build();
        EntityModel<UserDTO> entityModel = EntityModel.of(userDTO);
        given(userService.getUser(1)).willReturn(userDTO);
        given(userAssembler.toModel(userDTO)).willReturn(entityModel);
        //When
        mockMvc.perform(get("/user/1"))
                //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestName"))
                .andExpect(jsonPath("$.email").value("TestEmail@test.test"))
                .andExpect(jsonPath("$.age").value(34));

        verify(userService).getUser(1);
        verify(userAssembler).toModel(userDTO);
        verifyNoMoreInteractions(userService, userAssembler);
    }

    @Test
    @DisplayName("Получить не существующего пользователя по Id")
    void getUserByIdNotFound() throws Exception {
        //Given
        given(userService.getUser(1)).willThrow(new UserNotFound("Пользователь не был найден"));
        //When
        mockMvc.perform(get("/user/1"))
                //Then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.detail")
                        .value("Пользователь не был найден"));
        verify(userService).getUser(1);
        verifyNoMoreInteractions(userService);
    }


    @Test
    @DisplayName("Создание пользователя с корректными данными")
    void createUser() throws Exception {
        //Given
        UserDTOForCreateAndUpdate userDTOForCreateAndUpdate = UserDTOForCreateAndUpdate.builder()
                .name("NameA").email("EmailA@test.test").age(43).build();
        UserDTO userDTO = UserDTO.builder()
                .id(1).name("NameA").email("EmailA@test.test").age(43).build();
        EntityModel<UserDTO> entityModel = EntityModel.of(userDTO);
        given(userService.save(userDTOForCreateAndUpdate)).willReturn(userDTO);
        given(userAssembler.toModel(userDTO)).willReturn(entityModel);
        //When
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTOForCreateAndUpdate)))
                //Then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("NameA"))
                .andExpect(jsonPath("$.email").value("EmailA@test.test"))
                .andExpect(jsonPath("$.age").value(43));
        verify(userService).save(userDTOForCreateAndUpdate);
        verify(userAssembler).toModel(userDTO);
        verifyNoMoreInteractions(userService, userAssembler);
    }

    @Test
    @DisplayName("Создание пользователя с некорректными данными")
    public void createNoCorrectUser() throws Exception {
        //Given
        UserDTOForCreateAndUpdate userDTOForCreateAndUpdate = UserDTOForCreateAndUpdate.builder()
                .name("nameA").email("EmailA@test.test").age(43).build();
        given(userService.save(userDTOForCreateAndUpdate))
                .willThrow(new NoCorrectUser("Не корректные данные пользователя: Первая буква имени должна быть заглавной."));
        //When
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTOForCreateAndUpdate)))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail")
                        .value("Не корректные данные пользователя: Первая буква имени должна быть заглавной."));
        verify(userService).save(userDTOForCreateAndUpdate);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Обновление существующего пользователя")
    void updateUser() throws Exception {
        //Given
        UserDTOForCreateAndUpdate userDTOForCreateAndUpdate = UserDTOForCreateAndUpdate.builder()
                .name("NameA").email("EmailA@test.test").age(43).build();
        UserDTO userDTO = UserDTO.builder()
                .id(1).name("NameA").email("EmailA@test.test").age(43).build();
        EntityModel<UserDTO> entityModel = EntityModel.of(userDTO);
        given(userService.update(1, userDTOForCreateAndUpdate)).willReturn(userDTO);
        given(userAssembler.toModel(userDTO)).willReturn(entityModel);
        //When
        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTOForCreateAndUpdate)))
                //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("NameA"))
                .andExpect(jsonPath("$.email").value("EmailA@test.test"))
                .andExpect(jsonPath("$.age").value(43));
        verify(userService).update(1, userDTOForCreateAndUpdate);
        verify(userAssembler).toModel(userDTO);
        verifyNoMoreInteractions(userService, userAssembler);
    }

    @Test
    @DisplayName("Обновление не существующего пользователя")
    void updateUserNotFound() throws Exception {
        //Given
        UserDTOForCreateAndUpdate userDTOForCreateAndUpdate = UserDTOForCreateAndUpdate.builder()
                .name("NameA").email("EmailA@test.test").age(43).build();
        given(userService.update(eq(1), any(UserDTOForCreateAndUpdate.class)))
                .willThrow(new UserNotFound("Пользователь для обновления не найден"));
        //When
        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTOForCreateAndUpdate)))
                //Then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.detail").value("Пользователь не был найден"));
        verify(userService).update(eq(1), any(UserDTOForCreateAndUpdate.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Обновление существующего пользователя не корректными значениями")
    void updateNoCorrectUser() throws Exception {
        //Given
        UserDTOForCreateAndUpdate userDTOForCreateAndUpdate = UserDTOForCreateAndUpdate.builder()
                .name("ameA").email("EmailA@test.test").age(43).build();
        given(userService.update(eq(1), any(UserDTOForCreateAndUpdate.class)))
                .willThrow(new NoCorrectUser("Имя пользователя должно быть с большой буквы"));
        //When
        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTOForCreateAndUpdate)))
                //Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail")
                        .value("Имя пользователя должно быть с большой буквы"));
        verify(userService).update(eq(1), any(UserDTOForCreateAndUpdate.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Удаление существующего пользователя")
    void deleteUserById() throws Exception {
        //When
        mockMvc.perform(delete("/user/1"))
                //Then
                .andExpect(status().isNoContent());
        verify(userService).delete(1);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Удаление не существующего пользователя")
    void deleteUserByIdNotFound() throws Exception {
        //Given
        doThrow(new UserNotFound("Пользователь не был найден"))
                .when(userService).delete(1);
        //When
        mockMvc.perform(delete("/user/1"))
                //Then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.detail")
                        .value("Пользователь не был найден"));

        verify(userService).delete(1);
        verifyNoMoreInteractions(userService);
    }
}