package ru.zumber.learning.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.zumber.learning.notificationservice.dto.CreateMessageDTO;
import ru.zumber.learning.notificationservice.service.EmailService;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sendEmail")
    public ResponseEntity<Void> sendEmail(@RequestBody CreateMessageDTO createMessageDTO) {
        emailService.sendEmail(createMessageDTO.operation(), createMessageDTO.email());
        return ResponseEntity.ok().build();
    }

}
