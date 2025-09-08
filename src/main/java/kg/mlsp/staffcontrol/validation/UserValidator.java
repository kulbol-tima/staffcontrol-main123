package kg.mlsp.staffcontrol.validation;

import kg.mlsp.staffcontrol.dto.UserCreateDto;
import kg.mlsp.staffcontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    public void userCreateValidate(UserCreateDto dto) {
        if (dto.getEmail() == null || !dto.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Некорректный email");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
    }
}
