package kg.mlsp.staffcontrol.service;

import kg.mlsp.staffcontrol.dto.SubServiceCreateUpdateDto;
import kg.mlsp.staffcontrol.dto.SubServiceDto;
import kg.mlsp.staffcontrol.dto.SubServiceSearchDto;
import kg.mlsp.staffcontrol.mapper.SubServiceMapper;
import kg.mlsp.staffcontrol.model.SubService;
import kg.mlsp.staffcontrol.repository.SubServiceRepository;
import kg.mlsp.staffcontrol.spec.SubServiceSpec;
import kg.mlsp.staffcontrol.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class SubServiceServiceImpl implements SubServiceService {

    private final SubServiceRepository repository;
    private final SubServiceMapper mapper;
    private final LoggingService loggingService;

    public SubServiceServiceImpl(SubServiceRepository repository, SubServiceMapper mapper, LoggingService loggingService) {
        this.repository = repository;
        this.mapper = mapper;
        this.loggingService = loggingService;
    }

    @Override
    public Optional<SubServiceDto> getById(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }

    @Override
    public SubServiceDto create(SubServiceCreateUpdateDto subServiceCreateUpdateDto) {
        SubService subService = mapper.toEntity(subServiceCreateUpdateDto);
        SubService saved = repository.save(subService);
        SubServiceDto result = mapper.toDto(saved);
        
        // Логируем создание
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logCreate("SubService", saved.getId().toString(), result, null, username);
        
        return result;
    }

    @Override
    public Optional<SubServiceDto> update(Integer id, SubServiceCreateUpdateDto subServiceCreateUpdateDto) {
        return repository.findById(id)
                .map(existing -> {
                    // Сохраняем старые данные для логирования
                    SubServiceDto oldData = mapper.toDto(existing);
                    
                    mapper.updateEntityFromDto(subServiceCreateUpdateDto, existing);
                    SubService updated = repository.save(existing);
                    SubServiceDto newData = mapper.toDto(updated);
                    
                    // Логируем обновление
                    String username = SecurityUtils.getCurrentUsername().orElse("system");
                    loggingService.logUpdate("SubService", updated.getId().toString(), oldData, newData, null, username);
                    
                    return newData;
                });
    }

    @Override
    public boolean delete(Integer id) {
        if (repository.existsById(id)) {
            SubService subService = repository.findById(id).orElse(null);
            if (subService != null) {
                // Сохраняем данные для логирования
                SubServiceDto oldData = mapper.toDto(subService);
                
                repository.deleteById(id);
                
                // Логируем удаление
                String username = SecurityUtils.getCurrentUsername().orElse("system");
                loggingService.logDelete("SubService", id.toString(), oldData, null, username);
            }
            return true;
        }
        return false;
    }

    @Override
    public Page<SubServiceDto> filter(SubServiceSearchDto searchDto, Pageable pageable) {
        Specification<SubService> spec = Specification.where(null);
        if (searchDto.getNameRu() != null && !searchDto.getNameRu().isEmpty()) {
            spec = spec.and(SubServiceSpec.hasNameRu(searchDto.getNameRu()));
        }
        if (searchDto.getNameKy() != null && !searchDto.getNameKy().isEmpty()) {
            spec = spec.and(SubServiceSpec.hasNameKy(searchDto.getNameKy()));
        }
        if (searchDto.getCode() != null && !searchDto.getCode().isEmpty()) {
            spec = spec.and(SubServiceSpec.hasCode(searchDto.getCode()));
        }
        if (searchDto.getIsActive() != null) {
            spec = spec.and(SubServiceSpec.hasIsActive(searchDto.getIsActive()));
        }

        if (searchDto.getCreatedAtFrom() != null) {
            spec = spec.and(SubServiceSpec.hasCreatedAtFrom(searchDto.getCreatedAtFrom()));
        }
        if (searchDto.getCreatedAtTo() != null) {
            spec = spec.and(SubServiceSpec.hasCreatedAtTo(searchDto.getCreatedAtTo()));
        }
        if (searchDto.getUpdatedAtFrom() != null) {
            spec = spec.and(SubServiceSpec.hasUpdatedAtFrom(searchDto.getUpdatedAtFrom()));
        }
        if (searchDto.getUpdatedAtTo() != null) {
            spec = spec.and(SubServiceSpec.hasUpdatedAtTo(searchDto.getUpdatedAtTo()));
        }
        return repository.findAll(spec, pageable)
                .map(mapper::toDto);
    }

}