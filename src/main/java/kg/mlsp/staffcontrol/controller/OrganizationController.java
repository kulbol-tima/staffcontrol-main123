package kg.mlsp.staffcontrol.controller;

import jakarta.validation.Valid;
import kg.mlsp.staffcontrol.dto.OrganizationCreateUpdateDto;
import kg.mlsp.staffcontrol.dto.OrganizationDto;
import kg.mlsp.staffcontrol.dto.OrganizationSearchDto;

import kg.mlsp.staffcontrol.service.OrganizationService;
import kg.mlsp.staffcontrol.util.AccessPolicy;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/staffcontrol/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping
    public Page<OrganizationDto> getAllOrSearch(
            @ParameterObject @Valid OrganizationSearchDto searchDto,
            @ParameterObject Pageable pageable) {
        return organizationService.filter(searchDto, pageable);
    }


    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getById(@PathVariable Integer id) {
        OrganizationDto dto = organizationService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PostMapping
    public ResponseEntity<OrganizationDto> create(@RequestBody OrganizationCreateUpdateDto dto) {
        OrganizationDto created = organizationService.create(dto);
        return ResponseEntity.ok(created);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> update(@PathVariable Integer id, @RequestBody OrganizationCreateUpdateDto dto) {
        OrganizationDto updated = organizationService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
