package kg.mlsp.staffcontrol.controller;

import jakarta.validation.Valid;
import kg.mlsp.staffcontrol.dto.PositionDto;
import kg.mlsp.staffcontrol.dto.PositionSearchDto;
import kg.mlsp.staffcontrol.dto.request.PositionCreateUpdateDto;

import kg.mlsp.staffcontrol.service.PositionService;
import kg.mlsp.staffcontrol.util.AccessPolicy;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/staffcontrol/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @AccessPolicy(
            roles = {"SUPER_ADMIN"}
    )
    @GetMapping
    public Page<PositionDto> getAllOrFilter(
            @ParameterObject @Valid PositionSearchDto searchDto,
            @ParameterObject Pageable pageable) {
        return positionService.filter(searchDto, pageable);
    }


    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping("/{id}")
    public PositionDto getById(@PathVariable Integer id) {
        return positionService.getById(id);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PostMapping
    public PositionDto create(@RequestBody PositionCreateUpdateDto dto) {
        return positionService.create(dto);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PutMapping("/{id}")
    public PositionDto update(@PathVariable Integer id, @RequestBody PositionCreateUpdateDto dto) {
        return positionService.update(id, dto);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        positionService.delete(id);
    }
}

