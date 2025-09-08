package kg.mlsp.staffcontrol.service;

import kg.mlsp.staffcontrol.dto.ActionDto;
import kg.mlsp.staffcontrol.dto.ActionSearchDto;
import kg.mlsp.staffcontrol.dto.request.ActionCreateUpdateDto;
import kg.mlsp.staffcontrol.model.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ActionService {
    Page<Action> getAll(Pageable pageable);
    Optional<Action> getById(Integer id);
    ActionDto create(ActionCreateUpdateDto actionDto);
    Optional<ActionDto> update(Integer id, ActionCreateUpdateDto action);
    boolean delete(Integer id);
    Page<ActionDto> findByRoleId(Integer roleId, Pageable pageable);
    Page<ActionDto> filter(ActionSearchDto searchDto, Pageable pageable);
}