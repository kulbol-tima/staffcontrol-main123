package kg.mlsp.staffcontrol.service;

import kg.mlsp.staffcontrol.dto.SubServiceCreateUpdateDto;
import kg.mlsp.staffcontrol.dto.SubServiceDto;
import kg.mlsp.staffcontrol.dto.SubServiceSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SubServiceService {
    Page<SubServiceDto> filter(SubServiceSearchDto searchDto, Pageable pageable);

    Optional<SubServiceDto> getById(Integer id);

    SubServiceDto create(SubServiceCreateUpdateDto subServiceCreateUpdateDto);

    Optional<SubServiceDto> update(Integer id, SubServiceCreateUpdateDto subServiceCreateUpdateDto);

    boolean delete(Integer id);

}