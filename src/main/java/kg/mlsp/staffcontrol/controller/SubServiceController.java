package kg.mlsp.staffcontrol.controller;

import jakarta.validation.Valid;
import kg.mlsp.staffcontrol.dto.SubServiceCreateUpdateDto;
import kg.mlsp.staffcontrol.dto.SubServiceDto;
import kg.mlsp.staffcontrol.dto.SubServiceSearchDto;

import kg.mlsp.staffcontrol.service.SubServiceService;
import kg.mlsp.staffcontrol.util.AccessPolicy;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/staffcontrol/subservices")
public class SubServiceController {

    private final SubServiceService subServiceService;

    public SubServiceController(SubServiceService subServiceService) {
        this.subServiceService = subServiceService;
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping
    public Page<SubServiceDto> getAllOrFilter(
            @ParameterObject @Valid SubServiceSearchDto searchDto,
            @ParameterObject Pageable pageable) {
            return subServiceService.filter(searchDto, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubServiceDto> getById(@PathVariable Integer id) {
        return subServiceService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PostMapping
    public SubServiceDto create(@RequestBody SubServiceCreateUpdateDto subServiceCreateUpdateDto) {
        return subServiceService.create(subServiceCreateUpdateDto);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<SubServiceDto> update(@PathVariable Integer id, @RequestBody SubServiceCreateUpdateDto subServiceCreateUpdateDto) {
        return subServiceService.update(id, subServiceCreateUpdateDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (subServiceService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}