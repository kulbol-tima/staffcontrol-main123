package kg.mlsp.staffcontrol.controller;

import jakarta.validation.Valid;
import kg.mlsp.staffcontrol.dto.UserCreateDto;
import kg.mlsp.staffcontrol.dto.UserDto;
import kg.mlsp.staffcontrol.dto.UserSearchDto;
import kg.mlsp.staffcontrol.dto.UserUpdateDto;
import kg.mlsp.staffcontrol.dto.request.UserResetCredentialsDto;

import kg.mlsp.staffcontrol.service.UserService;
import kg.mlsp.staffcontrol.util.AccessPolicy;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/staffcontrol/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping
    public Page<UserDto> getAll(
            @ParameterObject @Valid UserSearchDto searchDto,
            @PageableDefault(size = 10) Pageable pageable) {
        if (searchDto == null || searchDto.isEmpty()) {
            return userService.getAll(pageable);
        }
        return userService.searchUsers(searchDto, pageable);
    }


    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PostMapping
    public UserDto create(@RequestBody UserCreateDto dto) {
        return userService.createUser(dto);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PutMapping("/{id}")
    public UserDto update(@PathVariable UUID id, @RequestBody UserUpdateDto dto) {
        return userService.updateUser(id, dto);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PutMapping("/{id}/reset-credentials")
    public UserDto resetCredentials(@PathVariable UUID id, @RequestBody @Valid UserResetCredentialsDto dto) {
        return userService.resetUserCredentials(id, dto);
    }
}

