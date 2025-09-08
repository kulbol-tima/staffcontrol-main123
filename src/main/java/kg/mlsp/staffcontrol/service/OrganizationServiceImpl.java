package kg.mlsp.staffcontrol.service;

import kg.mlsp.staffcontrol.dto.*;
import kg.mlsp.staffcontrol.spec.OrganizationSpec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import kg.mlsp.staffcontrol.mapper.OrganizationMapper;
import kg.mlsp.staffcontrol.model.Organization;
import kg.mlsp.staffcontrol.repository.OrganizationRepository;
import kg.mlsp.staffcontrol.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository repository;
    private final LoggingService loggingService;

    private final OrganizationMapper mapper;

    @Transactional(readOnly = true)
    public Page<OrganizationDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public OrganizationDto getById(Integer id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found")));
    }

    @Transactional
    public OrganizationDto create(OrganizationCreateUpdateDto dto) {
        Organization organization = mapper.toEntity(dto);
        Organization saved = repository.save(organization);
        OrganizationDto result = mapper.toDto(saved);
        
        // Логируем создание
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logCreate("Organization", saved.getId().toString(), result, null, username);
        
        return result;
    }

    @Transactional
    public OrganizationDto update(Integer id, OrganizationCreateUpdateDto dto) {
        Organization existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        // Сохраняем старые данные для логирования
        OrganizationDto oldData = mapper.toDto(existing);
        
        existing.setNameRu(dto.getNameRu());
        existing.setNameKy(dto.getNameKy());
        existing.setIsActive(dto.getIsActive());
        existing.setCode(dto.getCode());
        existing.setPin(dto.getPin());
        existing.setOrderNumber(dto.getOrderNumber());
        
        Organization saved = repository.save(existing);
        OrganizationDto newData = mapper.toDto(saved);
        
        // Логируем обновление
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logUpdate("Organization", saved.getId().toString(), oldData, newData, null, username);
        
        return newData;
    }

    @Transactional
    public void delete(Integer id) {
        Organization organization = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        // Сохраняем данные для логирования
        OrganizationDto oldData = mapper.toDto(organization);
        
        repository.deleteById(id);
        
        // Логируем удаление
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logDelete("Organization", id.toString(), oldData, null, username);
    }

    @Override
    public Page<OrganizationDto> filter(OrganizationSearchDto searchDto, Pageable pageable) {
        Specification<Organization> spec = Specification.where(null);
        if (searchDto.getNameRu() != null && !searchDto.getNameRu().isEmpty()) {
            spec = spec.and(OrganizationSpec.hasNameRu(searchDto.getNameRu()));
        }
        if (searchDto.getNameKy() != null && !searchDto.getNameKy().isEmpty()) {
            spec = spec.and(OrganizationSpec.hasNameKy(searchDto.getNameKy()));
        }
        if (searchDto.getCode() != null && !searchDto.getCode().isEmpty()) {
            spec = spec.and(OrganizationSpec.hasCode(searchDto.getCode()));
        }

        if (searchDto.getPin() != null && !searchDto.getPin().isEmpty()) {
            spec = spec.and(OrganizationSpec.hasPin(searchDto.getPin()));
        }

        if (searchDto.getIsActive() != null) {
            spec = spec.and(OrganizationSpec.hasIsActive(searchDto.getIsActive()));
        }

        if (searchDto.getCreatedAtFrom() != null) {
            spec = spec.and(OrganizationSpec.hasCreatedAtFrom(searchDto.getCreatedAtFrom()));
        }
        if (searchDto.getCreatedAtTo() != null) {
            spec = spec.and(OrganizationSpec.hasCreatedAtTo(searchDto.getCreatedAtTo()));
        }
        if (searchDto.getUpdatedAtFrom() != null) {
            spec = spec.and(OrganizationSpec.hasUpdatedAtFrom(searchDto.getUpdatedAtFrom()));
        }
        if (searchDto.getUpdatedAtTo() != null) {
            spec = spec.and(OrganizationSpec.hasUpdatedAtTo(searchDto.getUpdatedAtTo()));
        }
        return repository.findAll(spec, pageable)
                .map(mapper::toDto);
    }
}

