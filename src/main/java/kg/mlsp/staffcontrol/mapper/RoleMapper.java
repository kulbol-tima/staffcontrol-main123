package kg.mlsp.staffcontrol.mapper;

import kg.mlsp.staffcontrol.dto.RoleCreateUpdateDto;
import kg.mlsp.staffcontrol.dto.RoleDto;
import kg.mlsp.staffcontrol.model.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(Role role);
    Role toEntity(RoleCreateUpdateDto dto);
    List<RoleDto> toDtoList(List<Role> list);
}
