package kg.mlsp.staffcontrol.mapper;

import kg.mlsp.staffcontrol.dto.*;
import kg.mlsp.staffcontrol.model.UserSubServiceRoleAction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSubServiceRoleActionMapper2 {
    UserSubServiceRoleActionDto toDto(UserSubServiceRoleAction userSubServiceRoleAction);
}