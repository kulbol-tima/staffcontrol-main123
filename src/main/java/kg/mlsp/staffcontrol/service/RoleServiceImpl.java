package kg.mlsp.staffcontrol.service;


import kg.mlsp.staffcontrol.dto.*;
import kg.mlsp.staffcontrol.mapper.RoleMapper;
import kg.mlsp.staffcontrol.model.Role;
import kg.mlsp.staffcontrol.model.SubService;
import kg.mlsp.staffcontrol.repository.RoleRepository;
import kg.mlsp.staffcontrol.repository.SubServiceRepository;
import kg.mlsp.staffcontrol.spec.RoleSpec;
import kg.mlsp.staffcontrol.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final SubServiceRepository subServiceRepository;
    private final RoleMapper mapper;
    private final LoggingService loggingService;

    @Override
    public RoleDto getById(Integer id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found")));
    }

    @Override
    public RoleDto create(RoleCreateUpdateDto dto) {
        Role role = mapper.toEntity(dto);

        SubService subService = subServiceRepository.findById(dto.getSubServiceId()).orElseThrow(() -> new RuntimeException("SubService not found"));

        role.setSubService(subService);
        Role saved = repository.save(role);
        RoleDto result = mapper.toDto(saved);



        // Логируем создание
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logCreate("Role", saved.getId().toString(), result, null, username);
        
        return result;
    }

    @Override
    public RoleDto update(Integer id, RoleCreateUpdateDto dto) {
        Role existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        // Сохраняем старые данные для логирования
        RoleDto oldData = mapper.toDto(existing);
        
        existing.setNameKy(dto.getNameKy());
        existing.setNameRu(dto.getNameRu());
        existing.setCode(dto.getCode());
        existing.setIsActive(dto.getIsActive());
        existing.setSubService(subServiceRepository.findById(dto.getSubServiceId())
                .orElseThrow(() -> new RuntimeException("SubService not found")));
        
        Role saved = repository.save(existing);
        RoleDto newData = mapper.toDto(saved);
        
        // Логируем обновление
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logUpdate("Role", saved.getId().toString(), oldData, newData, null, username);
        
        return newData;
    }

    @Override
    public void delete(Integer id) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        // Сохраняем данные для логирования
        RoleDto oldData = mapper.toDto(role);
        
        repository.deleteById(id);
        
        // Логируем удаление
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logDelete("Role", id.toString(), oldData, null, username);
    }

    public Page<RoleDto> filter(RoleSearchDto searchDto, Pageable pageable) {
        Specification<Role> spec = Specification.where(null);
        if (searchDto.getNameRu() != null && !searchDto.getNameRu().isEmpty()) {
            spec = spec.and(RoleSpec.hasNameRu(searchDto.getNameRu()));
        }
        if (searchDto.getNameKy() != null && !searchDto.getNameKy().isEmpty()) {
            spec = spec.and(RoleSpec.hasNameKy(searchDto.getNameKy()));
        }
        if (searchDto.getCode() != null && !searchDto.getCode().isEmpty()) {
            spec = spec.and(RoleSpec.hasCode(searchDto.getCode()));
        }
        if (searchDto.getSubServiceId() != null) {
            spec = spec.and(RoleSpec.hasSubServiceId(searchDto.getSubServiceId()));
        }

        if (searchDto.getIsActive() != null) {
            spec = spec.and(RoleSpec.hasIsActive(searchDto.getIsActive()));
        }

        if (searchDto.getCreatedAtFrom() != null) {
            spec = spec.and(RoleSpec.hasCreatedAtFrom(searchDto.getCreatedAtFrom()));
        }
        if (searchDto.getCreatedAtTo() != null) {
            spec = spec.and(RoleSpec.hasCreatedAtTo(searchDto.getCreatedAtTo()));
        }
        if (searchDto.getUpdatedAtFrom() != null) {
            spec = spec.and(RoleSpec.hasUpdatedAtFrom(searchDto.getUpdatedAtFrom()));
        }
        if (searchDto.getUpdatedAtTo() != null) {
            spec = spec.and(RoleSpec.hasUpdatedAtTo(searchDto.getUpdatedAtTo()));
        }
        return repository.findAll(spec, pageable)
                .map(mapper::toDto);
    }

}