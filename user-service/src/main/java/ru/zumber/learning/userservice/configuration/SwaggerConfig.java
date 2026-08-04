package ru.zumber.learning.userservice.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI title() {
        Components components = new Components();

        components.addSchemas("ProblemDetail", new Schema<>()
                .description("Описание ошибки")
                .type("object")
                .addProperty("detail", new StringSchema())
                .addProperty("instance", new StringSchema().format("uri"))
                .addProperty("status", new IntegerSchema().format("int32"))
                .addProperty("title", new StringSchema()));

        components.addResponses(
                "400", new ApiResponse()
                        .description("Некорректные значения пользователя")
                        .content(contentProblemDetail()));

        components.addResponses("404", new ApiResponse()
                .description("Пользователь не найден")
                .content(contentProblemDetail()));

        components.addResponses("409", new ApiResponse()
                .description("Пользователь с данным Email уже зарегистрирован")
                .content(contentProblemDetail()));

        components.addResponses(
                "500", new ApiResponse()
                        .description("Ошибка сервера")
                        .content(contentProblemDetail())
        );
        return new OpenAPI()
                .servers(
                        List.of(new Server().url("http://localhost:8081")
                                .description("Локальный сервер для user-service"))
                )
                .info(new Info()
                        .title("User Service")
                        .description("Учебная программа в которой мы можем выполнять CRUD действия.")
                        .version("1.0")
                        .contact(new Contact().name("Ilya")))
                .components(components);
    }

    private Content contentProblemDetail() {
        return new Content().addMediaType(
                MediaType.APPLICATION_JSON_VALUE,
                new io.swagger.v3.oas.models.media.MediaType()
                        .schema(
                                new Schema<>()
                                        .$ref("#/components/schemas/ProblemDetail")
                        ));
    }
}
