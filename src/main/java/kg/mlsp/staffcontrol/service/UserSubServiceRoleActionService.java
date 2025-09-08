package kg.mlsp.staffcontrol.service;

import kg.mlsp.staffcontrol.dto.UserSubServiceRoleActionCreateDto;
import kg.mlsp.staffcontrol.dto.UserSubServiceRoleActionDto;
import kg.mlsp.staffcontrol.dto.UserSubServiceRoleActionSearchDto;
import kg.mlsp.staffcontrol.dto.request.PrivilegeActivationRequestDto;
import kg.mlsp.staffcontrol.dto.response.PrivilegeActivationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserSubServiceRoleActionService {
    void create(UserSubServiceRoleActionCreateDto dto);
    UserSubServiceRoleActionDto getByUserId(UUID userId);
    void update(UUID userId, UserSubServiceRoleActionCreateDto dto);
    Page<UserSubServiceRoleActionDto> filter(UserSubServiceRoleActionSearchDto searchDto, Pageable pageable);
    PrivilegeActivationResponseDto activateOrDeactivate(PrivilegeActivationRequestDto request);
}
