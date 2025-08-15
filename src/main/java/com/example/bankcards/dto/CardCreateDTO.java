package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardCreateDTO {

    @Schema(description = "Описание карты или комментарий", example = "Дебетовая зарплатная карта")
    @Size(min = 8, max = 255, message = "Длина описания должна быть от 8 до 255 символов")
    @NotBlank(message = "Описание карты не может быть пустым")
    private String description;

    @Schema(description = "Номер карты", example = "1111 2222 3333 4444 или 1234567890123456")
    @Size(min = 16, max = 19, message = "Номер карты представляет собой 16 цифр, допустимо разделять группы цифр пробелом или нижним подчеркиванием")
    @NotBlank(message = "Номер карты не может быть пустым")
    private String cardNumber;

    @Schema(description = "Кредитный лимит в виде целого положительного числа, может отсутствовать", example = "100_000 или 100000")
    @Size(min = 8, max = 255, message = "Длина описания должна быть от 8 до 255 символов")
    @Nullable
    @Positive
    private BigDecimal creditLimit;
}
