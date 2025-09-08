package kg.mlsp.staffcontrol.mapper;

import kg.mlsp.staffcontrol.dto.OrganizationCreateUpdateDto;
import kg.mlsp.staffcontrol.dto.OrganizationDto;
import kg.mlsp.staffcontrol.model.Organization;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    OrganizationDto toDto(Organization organization);
    Organization toEntity(OrganizationCreateUpdateDto dto);
    List<OrganizationDto> toDtoList(List<Organization> list);
}