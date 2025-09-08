package kg.mlsp.staffcontrol.service;


import kg.mlsp.staffcontrol.dto.PositionDto;
import kg.mlsp.staffcontrol.dto.PositionSearchDto;
import kg.mlsp.staffcontrol.dto.request.PositionCreateUpdateDto;
import kg.mlsp.staffcontrol.model.Position;
import kg.mlsp.staffcontrol.mapper.PositionMapper;
import kg.mlsp.staffcontrol.repository.PositionRepository;
import kg.mlsp.staffcontrol.spec.PositionSpec;
import kg.mlsp.staffcontrol.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository repository;
    private final PositionMapper mapper;
    private final LoggingService loggingService;

    public PositionDto getById(Integer id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found")));
    }

    public PositionDto create(PositionCreateUpdateDto dto) {
        Position position = mapper.toEntity(dto);
        Position saved = repository.save(position);
        PositionDto result = mapper.toDto(saved);
        
        // Логируем создание
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logCreate("Position", saved.getId().toString(), result, null, username);
        
        return result;
    }

    public PositionDto update(Integer id, PositionCreateUpdateDto dto) {
        Position existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        
        // Сохраняем старые данные для логирования
        PositionDto oldData = mapper.toDto(existing);
        
        existing.setNameRu(dto.getNameRu());
        existing.setNameKy(dto.getNameKy());
        existing.setIsActive(dto.getIsActive());
        existing.setOrderNumber(dto.getOrderNumber());
        existing.setCode(dto.getCode());
        
        Position saved = repository.save(existing);
        PositionDto newData = mapper.toDto(saved);
        
        // Логируем обновление
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logUpdate("Position", saved.getId().toString(), oldData, newData, null, username);
        
        return newData;
    }

    public void delete(Integer id) {
        Position position = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        
        // Сохраняем данные для логирования
        PositionDto oldData = mapper.toDto(position);
        
        repository.deleteById(id);
        
        // Логируем удаление
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logDelete("Position", id.toString(), oldData, null, username);
    }

    public Page<PositionDto> filter(PositionSearchDto searchDto, Pageable pageable) {
        Specification<Position> spec = Specification.where(null);
        if (searchDto.getNameRu() != null && !searchDto.getNameRu().isEmpty()) {
            spec = spec.and(PositionSpec.hasNameRu(searchDto.getNameRu()));
        }
        if (searchDto.getNameKy() != null && !searchDto.getNameKy().isEmpty()) {
            spec = spec.and(PositionSpec.hasNameKy(searchDto.getNameKy()));
        }
        if (searchDto.getCode() != null && !searchDto.getCode().isEmpty()) {
            spec = spec.and(PositionSpec.hasCode(searchDto.getCode()));
        }

        if (searchDto.getIsActive() != null) {
            spec = spec.and(PositionSpec.hasIsActive(searchDto.getIsActive()));
        }

        if (searchDto.getCreatedAtFrom() != null) {
            spec = spec.and(PositionSpec.hasCreatedAtFrom(searchDto.getCreatedAtFrom()));
        }
        if (searchDto.getCreatedAtTo() != null) {
            spec = spec.and(PositionSpec.hasCreatedAtTo(searchDto.getCreatedAtTo()));
        }
        if (searchDto.getUpdatedAtFrom() != null) {
            spec = spec.and(PositionSpec.hasUpdatedAtFrom(searchDto.getUpdatedAtFrom()));
        }
        if (searchDto.getUpdatedAtTo() != null) {
            spec = spec.and(PositionSpec.hasUpdatedAtTo(searchDto.getUpdatedAtTo()));
        }
        return repository.findAll(spec, pageable)
                .map(mapper::toDto);
    }

}

