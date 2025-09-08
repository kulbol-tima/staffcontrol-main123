package kg.mlsp.staffcontrol.service;

import kg.mlsp.staffcontrol.dto.PositionDto;
import kg.mlsp.staffcontrol.dto.PositionSearchDto;
import kg.mlsp.staffcontrol.dto.request.PositionCreateUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PositionService {
    Page<PositionDto> filter(PositionSearchDto searchDto, Pageable pageable);
    PositionDto getById(Integer id);
    PositionDto create(PositionCreateUpdateDto dto);
    PositionDto update(Integer id, PositionCreateUpdateDto dto);
    void delete(Integer id);
}
