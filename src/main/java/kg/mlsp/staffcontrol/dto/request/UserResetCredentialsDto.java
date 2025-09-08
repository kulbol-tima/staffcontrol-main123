package kg.mlsp.staffcontrol.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserResetCredentialsDto {
    
    @NotBlank(message = "Новый логин обязателен")
    @Size(min = 3, max = 100, message = "Логин должен содержать от 3 до 100 символов")
    private String newUsername;
    
    @NotBlank(message = "Новый пароль обязателен")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String newPassword;
}

