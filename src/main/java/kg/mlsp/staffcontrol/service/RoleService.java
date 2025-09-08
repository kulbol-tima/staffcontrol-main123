package kg.mlsp.staffcontrol.service;

import kg.mlsp.staffcontrol.dto.RoleCreateUpdateDto;
import kg.mlsp.staffcontrol.dto.RoleDto;
import kg.mlsp.staffcontrol.dto.RoleSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RoleService {
    Page<RoleDto> filter(RoleSearchDto searchDto, Pageable pageable);
    RoleDto getById(Integer id);
    RoleDto create(RoleCreateUpdateDto dto);
    RoleDto update(Integer id, RoleCreateUpdateDto dto);
    void delete(Integer id);
}