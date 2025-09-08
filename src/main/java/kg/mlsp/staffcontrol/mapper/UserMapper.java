package kg.mlsp.staffcontrol.mapper;

import kg.mlsp.staffcontrol.dto.UserDto;
import kg.mlsp.staffcontrol.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" , uses = { StaffMapper.class })
public interface UserMapper {

    UserDto toDto(User user);
}