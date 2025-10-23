package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @Schema(description = "Старый пароль", example = "AbcdaBCD_094")
    private String oldPassword;
    @Schema(description = "Новый пароль", example = "ZuwChh_344a")
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    private String newPassword;
    @Schema(description = "Подтверждение пароля", example = "ZuwChh_344a")
    @Size(min = 8, max = 255, message = "Новый и подтвержденный пароль должны совпадать")
    private String newPasswordConfirmed;
}
