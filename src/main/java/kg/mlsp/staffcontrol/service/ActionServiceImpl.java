package kg.mlsp.staffcontrol.service;

import kg.mlsp.staffcontrol.dto.ActionDto;
import kg.mlsp.staffcontrol.dto.ActionSearchDto;
import kg.mlsp.staffcontrol.dto.request.ActionCreateUpdateDto;
import kg.mlsp.staffcontrol.mapper.ActionMapper;
import kg.mlsp.staffcontrol.model.Action;
import kg.mlsp.staffcontrol.model.Role;
import kg.mlsp.staffcontrol.repository.ActionRepository;
import kg.mlsp.staffcontrol.repository.RoleRepository;
import kg.mlsp.staffcontrol.spec.ActionSpec;
import kg.mlsp.staffcontrol.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActionServiceImpl implements ActionService {

    private final ActionRepository repository;
    private final ActionMapper mapper;
    private final LoggingService loggingService;
    private final RoleRepository roleRepository;

    public ActionServiceImpl(ActionRepository repository, ActionMapper mapper, LoggingService loggingService,  RoleRepository roleRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.loggingService = loggingService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<Action> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Action> getById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public ActionDto create(ActionCreateUpdateDto actionCreateUpdateDto) {
        Action action = mapper.toEntity(actionCreateUpdateDto);
        Role role = roleRepository.findById(actionCreateUpdateDto.getRoleId()).orElseThrow(() -> new RuntimeException("SubService not found"));
        action.setRole(role);
        Action saved = repository.save(action);
        ActionDto result = mapper.toDto(saved);
        
        // Логируем создание
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logCreate("Action", saved.getId().toString(), result, null, username);
        
        return result;
    }

    @Override
    public Optional<ActionDto> update(Integer id, ActionCreateUpdateDto actionDto) {
        return repository.findById(id)
                .map(existing -> {
                    // Сохраняем старые данные для логирования
                    ActionDto oldData = mapper.toDto(existing);
                    
                    mapper.updateEntityFromDto(actionDto, existing);
                    Action updated = repository.save(existing);
                    ActionDto newData = mapper.toDto(updated);
                    
                    // Логируем обновление
                    String username = SecurityUtils.getCurrentUsername().orElse("system");
                    loggingService.logUpdate("Action", updated.getId().toString(), oldData, newData, null, username);
                    
                    return newData;
                });
    }

    @Override
    public boolean delete(Integer id) {
        if (repository.existsById(id)) {
            Action action = repository.findById(id).orElse(null);
            if (action != null) {
                // Сохраняем данные для логирования
                ActionDto oldData = mapper.toDto(action);
                
                repository.deleteById(id);
                
                // Логируем удаление
                String username = SecurityUtils.getCurrentUsername().orElse("system");
                loggingService.logDelete("Action", id.toString(), oldData, null, username);
            }
            return true;
        }
        return false;
    }

    @Override
    public Page<ActionDto> findByRoleId(Integer roleId, Pageable pageable) {
        Page<Action> actionsPage = repository.findByRoleId(roleId, pageable);
        return actionsPage.map(mapper::toDto);
    }

    @Override
    public Page<ActionDto> filter(ActionSearchDto searchDto, Pageable pageable) {
        Specification<Action> spec = Specification.where(null);
        if (searchDto.getNameRu() != null && !searchDto.getNameRu().isEmpty()) {
            spec = spec.and(ActionSpec.hasNameRu(searchDto.getNameRu()));
        }
        if (searchDto.getNameKy() != null && !searchDto.getNameKy().isEmpty()) {
            spec = spec.and(ActionSpec.hasNameKy(searchDto.getNameKy()));
        }
        if (searchDto.getCode() != null && !searchDto.getCode().isEmpty()) {
            spec = spec.and(ActionSpec.hasCode(searchDto.getCode()));
        }
        if (searchDto.getCreatedAtFrom() != null) {
            spec = spec.and(ActionSpec.hasCreatedAtFrom(searchDto.getCreatedAtFrom()));
        }
        if (searchDto.getCreatedAtTo() != null) {
            spec = spec.and(ActionSpec.hasCreatedAtTo(searchDto.getCreatedAtTo()));
        }
        if (searchDto.getUpdatedAtFrom() != null) {
            spec = spec.and(ActionSpec.hasUpdatedAtFrom(searchDto.getUpdatedAtFrom()));
        }
        if (searchDto.getUpdatedAtTo() != null) {
            spec = spec.and(ActionSpec.hasUpdatedAtTo(searchDto.getUpdatedAtTo()));
        }
        if (searchDto.getRoleId() != null) {
            spec = spec.and(ActionSpec.hasRoleId(searchDto.getRoleId()));
        }
        return repository.findAll(spec, pageable)
                .map(mapper::toDto);
    }
}