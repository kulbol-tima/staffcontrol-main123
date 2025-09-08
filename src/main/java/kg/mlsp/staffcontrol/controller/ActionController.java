package kg.mlsp.staffcontrol.controller;

import jakarta.validation.Valid;
import kg.mlsp.staffcontrol.dto.ActionDto;
import kg.mlsp.staffcontrol.dto.ActionSearchDto;
import kg.mlsp.staffcontrol.dto.request.ActionCreateUpdateDto;

import kg.mlsp.staffcontrol.model.Action;
import kg.mlsp.staffcontrol.service.ActionService;
import kg.mlsp.staffcontrol.util.AccessPolicy;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/staffcontrol/actions")
public class ActionController {

    private final ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping
    public Page<ActionDto> filter(@ParameterObject @Valid ActionSearchDto searchDto,
                                  @ParameterObject Pageable pageable) {
        return actionService.filter(searchDto, pageable);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<Action> getById(@PathVariable Integer id) {
        return actionService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PostMapping
    public ActionDto create(@RequestBody ActionCreateUpdateDto dto) {
        return actionService.create(dto);
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @PutMapping("/{id}")
    public ActionDto update(@PathVariable Integer id, @RequestBody ActionCreateUpdateDto dto) {
        return actionService.update(id, dto)
                .orElseThrow(() -> new RuntimeException("Action not found with id: " + id));
    }

    @AccessPolicy(roles = {"SUPER_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (actionService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

//    @AccessPolicy(roles = {"SUPER_ADMIN"})
//    @GetMapping("/by-role/{roleId}")
//    public Page<ActionDto> getActionsByRole(@PathVariable Integer roleId,
//                                           @ParameterObject Pageable pageable) {
//        return actionService.findByRoleId(roleId, pageable);
//    }
}