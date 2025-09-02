package com.example.bankcards.dto;

import com.example.bankcards.entity.UserRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestCreateDTO {
    @Schema(description = "Идентификатор карты")
    @NotBlank(message = "Идентификатор карты обязателен")
    private Long cardId;

    @Schema(description = "Тип запроса")
    @NotBlank(message = "Тип запроса обязателен")
    private UserRequest.Type type;

    @Schema(description = "Комментарий к запросу")
    @Size(min = 16, max = 100, message = "Пояснения о причинах запроса, от 16 до 100 символов")
    @NotBlank(message = "Комментарий обязателен")
    private String comment;
}
