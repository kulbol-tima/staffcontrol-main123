package kg.mlsp.staffcontrol.mapper;

import kg.mlsp.staffcontrol.dto.RoleWithActionsDto;
import kg.mlsp.staffcontrol.dto.SubServiceWithRolesDto;
import kg.mlsp.staffcontrol.dto.UserDto;
import kg.mlsp.staffcontrol.dto.UserSubServiceRoleActionDto;
import kg.mlsp.staffcontrol.model.Action;
import kg.mlsp.staffcontrol.model.Role;
import kg.mlsp.staffcontrol.model.SubService;
import kg.mlsp.staffcontrol.model.UserSubServiceRoleAction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserSubServiceRoleActionMapper {
    private ActionMapper actionMapper;
    public UserSubServiceRoleActionMapper(
            UserMapper userMapper,
            SubServiceMapper subServiceMapper,
            RoleMapper roleMapper,
            ActionMapper actionMapper
    ) {
        this.actionMapper = actionMapper;
    }
    public UserSubServiceRoleActionDto toDtoByUser(List<UserSubServiceRoleAction> entities, UserDto userDto) {
        if (entities == null || entities.isEmpty()) {
            return new UserSubServiceRoleActionDto(userDto, new ArrayList<>());
        }

        Map<Integer, SubServiceWithRolesDto> subServiceMap = new HashMap<>();

        for (UserSubServiceRoleAction entity : entities) {
            SubService subService = entity.getSubService();
            Integer subServiceId = subService.getId();
            SubServiceWithRolesDto subServiceDto = subServiceMap.computeIfAbsent(
                    subServiceId,
                    id -> new SubServiceWithRolesDto(
                            subService.getId(),
                            subService.getNameRu(),
                            subService.getNameKy(),
                            subService.getIsActive(),
                            subService.getOrderNumber(),
                            subService.getCode(),
                            new ArrayList<>()
                    )
            );

            Integer roleId = entity.getRole().getId();
            RoleWithActionsDto roleDto = subServiceDto.getRoles().stream()
                    .filter(r -> r.getId().equals(roleId))
                    .findFirst()
                    .orElseGet(() -> {
                        Role role = entity.getRole();
                        RoleWithActionsDto newRole = new RoleWithActionsDto(
                                role.getId(),
                                role.getNameRu(),
                                role.getNameKy(),
                                role.getCode(),
                                role.getOrderNumber(),
                                new ArrayList<>()
                        );
                        subServiceDto.getRoles().add(newRole);
                        return newRole;
                    });

            if (entity.getAction() != null) {
                Action action = entity.getAction();
                roleDto.getActions().add(actionMapper.toDto(action));
            }
        }

        return new UserSubServiceRoleActionDto(userDto, new ArrayList<>(subServiceMap.values()));
    }
}