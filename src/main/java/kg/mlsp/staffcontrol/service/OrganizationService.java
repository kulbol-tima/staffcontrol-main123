package kg.mlsp.staffcontrol.service;

import kg.mlsp.staffcontrol.dto.OrganizationCreateUpdateDto;
import kg.mlsp.staffcontrol.dto.OrganizationDto;
import kg.mlsp.staffcontrol.dto.OrganizationSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrganizationService {
    Page<OrganizationDto> getAll(Pageable pageable);
    OrganizationDto getById(Integer id);
    OrganizationDto create(OrganizationCreateUpdateDto dto);
    OrganizationDto update(Integer id, OrganizationCreateUpdateDto dto);
    void delete(Integer id);
    Page<OrganizationDto> filter(OrganizationSearchDto searchDto, Pageable pageable);
}
