package kg.mlsp.staffcontrol.mapper;

import kg.mlsp.staffcontrol.dto.ActionDto;
import kg.mlsp.staffcontrol.dto.request.ActionCreateUpdateDto;
import kg.mlsp.staffcontrol.model.Action;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActionMapper {
    ActionDto toDto(Action action);
    Action toEntity(ActionCreateUpdateDto dto);
    void updateEntityFromDto(ActionCreateUpdateDto dto, @MappingTarget Action entity);
    List<ActionDto> toDtoList(List<Action> list);
}