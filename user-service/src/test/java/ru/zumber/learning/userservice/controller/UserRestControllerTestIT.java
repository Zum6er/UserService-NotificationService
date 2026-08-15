package ru.zumber.learning.userservice.controller;

import ru.zumber.learning.userservice.dto.UserDTOForCreateAndUpdate;
import ru.zumber.learning.userservice.entity.User;
import ru.zumber.learning.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRestControllerTestIT extends AbstractControllerForIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        userRepository.saveAll(List.of(UserUtils.getUserA(), UserUtils.getUserB(), UserUtils.getUserC()));
    }

    @Test
    @DisplayName("Сохранение нового пользователя")
    void saveUser() throws Exception {
        //Given
        UserDTOForCreateAndUpdate userForCreate = UserDTOForCreateAndUpdate.builder()
                .name("NameD")
                .email("EmailD@test.test")
                .age(24)
                .build();
        //When
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userForCreate)))
                //Then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("NameD"))
                .andExpect(jsonPath("$.email").value("EmailD@test.test"))
                .andExpect(jsonPath("$.age").value(24));

    }

    @Test
    @DisplayName("Получение пользователя по id")
    void getUserById() throws Exception {
        //Given
        Integer id = userRepository.findAll().get(0).getId();
        //When
        mockMvc.perform(get("/user/{id}", id))
                //Then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("NameA"))
                .andExpect(jsonPath("$.email").value("emailA@test.test"))
                .andExpect(jsonPath("$.age").value(34));
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    void getAllUsers() throws Exception {
        //When
        mockMvc.perform(get("/user"))
                //Then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userDTOList").isArray())
                .andExpect(jsonPath("$._embedded.userDTOList.length()").value(3))
                .andExpect(jsonPath("$._embedded.userDTOList[0].id").exists())
                .andExpect(jsonPath("$._embedded.userDTOList[1].id").isNumber())
                .andExpect(jsonPath("$._embedded.userDTOList[1].name").value("NameB"))
                .andExpect(jsonPath("$._embedded.userDTOList[2].email").value("emailC@test.test"))
                .andExpect(jsonPath("$._embedded.userDTOList[0].age").value(34));
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    void updateUser() throws Exception {
        //Given
        UserDTOForCreateAndUpdate userForUpdate = UserDTOForCreateAndUpdate.builder()
                .name("UpdateName")
                .email("updateEmail@test.test")
                .age(35)
                .build();
        Integer id = userRepository.findAll()
                .stream()
                .filter(u -> u.getName().equals("NameA"))
                .findFirst()
                .get()
                .getId();
        //When
        mockMvc.perform(put("/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userForUpdate)))
                //Then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("UpdateName"))
                .andExpect(jsonPath("$.email").value("updateEmail@test.test"))
                .andExpect(jsonPath("$.age").value(35));
    }

    @Test
    @DisplayName("Удаление пользователя")
    void deleteUser() throws Exception {
        Integer id = userRepository.findAll()
                .stream()
                .filter(u -> u.getName().equals("NameA"))
                .map(User::getId)
                .findFirst()
                        .orElse(null);
        //When
        mockMvc.perform(delete("/user/{id}", id))
                //Then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
        assertThat(userRepository.findById(1)).isEmpty();
        assertThat(userRepository.count()).isEqualTo(2);
    }
}