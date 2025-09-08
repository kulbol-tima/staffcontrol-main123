package kg.mlsp.staffcontrol.mapper;

import kg.mlsp.staffcontrol.dto.PositionDto;
import kg.mlsp.staffcontrol.dto.request.PositionCreateUpdateDto;
import kg.mlsp.staffcontrol.model.Position;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    PositionDto toDto(Position position);
    Position toEntity(PositionCreateUpdateDto dto);
    List<PositionDto> toDtoList(List<Position> list);
}