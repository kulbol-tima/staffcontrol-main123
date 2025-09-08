package kg.mlsp.staffcontrol.mapper;

import kg.mlsp.staffcontrol.dto.SubServiceCreateUpdateDto;
import kg.mlsp.staffcontrol.dto.SubServiceDto;
import kg.mlsp.staffcontrol.model.SubService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubServiceMapper {
    SubServiceDto toDto(SubService subService);
    SubService toEntity(SubServiceCreateUpdateDto dto);
    List<SubServiceDto> toDtoList(List<SubService> list);
    SubService updateEntityFromDto(SubServiceCreateUpdateDto dto, @MappingTarget SubService entity);
}