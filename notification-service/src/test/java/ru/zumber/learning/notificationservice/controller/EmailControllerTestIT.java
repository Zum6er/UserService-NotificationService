package ru.zumber.learning.notificationservice.controller;


import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.zumber.learning.formessagedto.Operation;
import ru.zumber.learning.notificationservice.dto.CreateMessageDTO;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmailControllerTestIT extends AbstractEmailControllerIT {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Отправляем сообщение о создание пользователя")
    public void sendEmailCreateUser() throws Exception {
        //Given
        CreateMessageDTO messageDTO = new CreateMessageDTO(Operation.CREATE, "test1@test.test");
        //When
        mockMvc.perform(post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageDTO)))
                //Then
                .andExpect(status().isOk());
        MimeMessage[] messages = greenMail.getReceivedMessages();

        assertNotNull(messages);
        assertEquals(1, messages.length);

        MimeMessage message = messages[0];

        assertEquals("test1@test.test", message.getAllRecipients()[0].toString());
        assertEquals("Сохранение пользователя", message.getSubject());

        String body = message.getContent().toString();
        assertTrue(body.contains("успешно сохранен"));
    }

    @Test
    @DisplayName("Отправляем сообщение о удаление пользователя")
    public void sendEmailDeleteUser() throws Exception {
        //Given
        CreateMessageDTO messageDTO = new CreateMessageDTO(Operation.DELETE, "test1@test.test");
        //When
        mockMvc.perform(post("/sendEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDTO)))
        //Then
                .andExpect(status().isOk());
        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);

        MimeMessage message = messages[0];
        assertEquals("test1@test.test", message.getAllRecipients()[0].toString());
        assertEquals("Удаление пользователя", message.getSubject());

        String body = message.getContent().toString();
        assertTrue(body.contains("был удален"));
    }

}