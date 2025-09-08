package kg.mlsp.staffcontrol.mapper;

import kg.mlsp.staffcontrol.dto.StaffDto;
import kg.mlsp.staffcontrol.model.Staff;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    Staff toEntity(StaffDto dto);

    StaffDto toDto(Staff entity);

    List<StaffDto> toDtoList(List<Staff> list);
}