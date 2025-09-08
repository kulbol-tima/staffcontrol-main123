package kg.mlsp.staffcontrol.service;

import kg.mlsp.staffcontrol.dto.UserCreateDto;
import kg.mlsp.staffcontrol.dto.UserDto;
import kg.mlsp.staffcontrol.dto.UserSearchDto;
import kg.mlsp.staffcontrol.dto.UserUpdateDto;
import kg.mlsp.staffcontrol.dto.request.UserResetCredentialsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    Page<UserDto> getAll(Pageable pageable);
    Page<UserDto> searchUsers(UserSearchDto searchDto, Pageable pageable);
    UserDto getById(UUID id);
    UserDto createUser(UserCreateDto dto);
    UserDto updateUser(UUID id, UserUpdateDto dto);
    void deleteUser(UUID id);
    void changePassword(String username, String oldPassword, String newPassword);
    UserDto resetUserCredentials(UUID id, UserResetCredentialsDto dto);
}