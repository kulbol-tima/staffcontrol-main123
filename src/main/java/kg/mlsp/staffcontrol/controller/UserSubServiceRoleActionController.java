package kg.mlsp.staffcontrol.controller;

import jakarta.validation.Valid;
import kg.mlsp.staffcontrol.dto.*;

import kg.mlsp.staffcontrol.dto.request.PrivilegeActivationRequestDto;
import kg.mlsp.staffcontrol.dto.response.PrivilegeActivationResponseDto;
import kg.mlsp.staffcontrol.service.UserSubServiceRoleActionService;
import kg.mlsp.staffcontrol.util.AccessPolicy;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/staffcontrol/user-sub-service-role-action")
public class UserSubServiceRoleActionController {

    private final UserSubServiceRoleActionService service;

    public UserSubServiceRoleActionController(UserSubServiceRoleActionService service) {
        this.service = service;
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping
    public Page<UserSubServiceRoleActionDto> getAllOrFilter(@ParameterObject @Valid UserSubServiceRoleActionSearchDto searchDto,
                                                            @ParameterObject Pageable pageable) {
        return service.filter(searchDto, pageable);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping("/{userId}")
    public UserSubServiceRoleActionDto getByUserId(@PathVariable UUID userId) {
        return service.getByUserId(userId);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PutMapping("/{userId}")
    public void update(@PathVariable UUID userId, @RequestBody UserSubServiceRoleActionCreateDto dto) {
        service.update(userId, dto);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PostMapping("/activation")
    public ResponseEntity<PrivilegeActivationResponseDto> activateOrDeactivate(
            @RequestBody @Valid PrivilegeActivationRequestDto request
    ) {
        PrivilegeActivationResponseDto resp = service.activateOrDeactivate(request);
        return ResponseEntity.ok(resp);
    }
}
