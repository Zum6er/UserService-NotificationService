package ru.zumber.learning.notificationservice.controller;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AbstractEmailControllerIT {

    private static final ServerSetup serverSetup =
//            ServerSetup.SMTP.dynamicPort();
            new ServerSetup(3025, null, ServerSetup.PROTOCOL_SMTP);

    @RegisterExtension
    static GreenMailExtension greenMail =
            new GreenMailExtension(serverSetup)
                    .withConfiguration(
                            GreenMailConfiguration.aConfig()
                                    .withUser("test@test.test", "test")
                    );
//            .withPerMethodLifecycle(false);

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", serverSetup::getPort);
        registry.add("spring.mail.username", () -> "test@test.test");
        registry.add("spring.mail.password", () -> "test");
    }

}
