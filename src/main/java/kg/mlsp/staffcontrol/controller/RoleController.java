package kg.mlsp.staffcontrol.controller;

import jakarta.validation.Valid;
import kg.mlsp.staffcontrol.dto.RoleCreateUpdateDto;
import kg.mlsp.staffcontrol.dto.RoleDto;
import kg.mlsp.staffcontrol.dto.RoleSearchDto;

import kg.mlsp.staffcontrol.service.RoleService;
import kg.mlsp.staffcontrol.util.AccessPolicy;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/staffcontrol/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping
    public Page<RoleDto> getAllOrFilter(
            @ParameterObject @Valid RoleSearchDto searchDto,
            @ParameterObject Pageable pageable) {
        return roleService.filter(searchDto, pageable);
    }


    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping("/{id}")
    public RoleDto getById(@PathVariable Integer id) {
        return roleService.getById(id);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PostMapping
    public RoleDto create(@RequestBody RoleCreateUpdateDto dto) {
        return roleService.create(dto);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PutMapping("/{id}")
    public RoleDto update(@PathVariable Integer id, @RequestBody RoleCreateUpdateDto dto) {
        return roleService.update(id, dto);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        roleService.delete(id);
    }

}
