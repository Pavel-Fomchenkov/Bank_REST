package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionCreateDTO {
    @Schema(description = "Идентификатор карты списания")
    @NotBlank(message = "Идентификатор карты не может быть пустым")
    private long fromCardId;
    @Schema(description = "Идентификатор карты зачисления")
    @NotBlank(message = "Идентификатор карты не может быть пустым")
    private long toCardId;
    @Schema(description = "Сумма перевода")
    @NotBlank(message = "Положительное число, максимум 2 знака после запятой")
    @Positive
    private BigDecimal sum;
}
