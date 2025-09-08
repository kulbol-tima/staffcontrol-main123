package kg.mlsp.staffcontrol.service;

import kg.mlsp.staffcontrol.dto.*;
import kg.mlsp.staffcontrol.dto.request.PrivilegeActivationRequestDto;
import kg.mlsp.staffcontrol.dto.request.SubServiceIdsWithRolesDto;
import kg.mlsp.staffcontrol.dto.request.RoleIdsWithActionDto;
import kg.mlsp.staffcontrol.dto.response.PrivilegeActivationResponseDto;
import kg.mlsp.staffcontrol.exception.ResourceNotFoundException;
import kg.mlsp.staffcontrol.mapper.UserMapper;
import kg.mlsp.staffcontrol.mapper.UserSubServiceRoleActionMapper;
import kg.mlsp.staffcontrol.mapper.UserSubServiceRoleActionMapper2;
import kg.mlsp.staffcontrol.model.*;
import kg.mlsp.staffcontrol.repository.*;
import kg.mlsp.staffcontrol.spec.UserSubServiceRoleActionSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserSubServiceRoleActionServiceImpl implements UserSubServiceRoleActionService {

    private final UserSubServiceRoleActionRepository repository;
    private final UserRepository userRepository;
    private final SubServiceRepository subServiceRepository;
    private final RoleRepository roleRepository;
    private final ActionRepository actionRepository;
    private final UserSubServiceRoleActionMapper mapper;
    private final UserSubServiceRoleActionMapper2 mapper2;
    private final UserMapper userMapper;

    public UserSubServiceRoleActionServiceImpl(
            UserSubServiceRoleActionRepository repository,
            UserRepository userRepository,
            SubServiceRepository subServiceRepository,
            RoleRepository roleRepository,
            ActionRepository actionRepository,
            UserSubServiceRoleActionMapper mapper, UserMapper userMapper,
            UserSubServiceRoleActionMapper2 mapper2
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.subServiceRepository = subServiceRepository;
        this.roleRepository = roleRepository;
        this.actionRepository = actionRepository;
        this.mapper = mapper;
        this.userMapper = userMapper;
        this.mapper2 = mapper2;
    }

    @Transactional
    @Override
    public void create(UserSubServiceRoleActionCreateDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<UserSubServiceRoleAction> entities = new ArrayList<>();

        for (SubServiceIdsWithRolesDto subServiceDto : dto.getSubServiceId()) {

            SubService subService = subServiceRepository.findById(subServiceDto.getId()).orElseThrow(() -> new ResourceNotFoundException("SubService not found"));

            for (RoleIdsWithActionDto roleDto : subServiceDto.getRoleIds()) {
                Role role = roleRepository.findById(roleDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Role not found"));

                if (roleDto.getActionIds() != null && !roleDto.getActionIds().isEmpty()) {
                    for (Integer actionId : roleDto.getActionIds()) {
                        Action action = actionRepository.findById(actionId).orElseThrow(() -> new ResourceNotFoundException("Action not found"));
                        entities.add(new UserSubServiceRoleAction(null, user, subService, role, action, true, LocalDateTime.now(), null,  null));
                    }
                } else {
                    entities.add(new UserSubServiceRoleAction(null, user, subService, role, null,  true, LocalDateTime.now(), null,  null));
                }
            }
            if (subServiceDto.getRoleIds() == null)
            {
                entities.add(new UserSubServiceRoleAction(null, user, subService, null, null,   true, LocalDateTime.now(), null,  null));
            }
        }

        repository.saveAll(entities);
    }

    @Transactional
    public void createIfNotExist(UUID userId, UserSubServiceRoleActionCreateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<UserSubServiceRoleAction> toSaveOrUpdate = new ArrayList<>();

        for (SubServiceIdsWithRolesDto subServiceDto : dto.getSubServiceId()) {
            SubService subService = subServiceRepository.findById(subServiceDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("SubService not found"));

            for (RoleIdsWithActionDto roleDto : subServiceDto.getRoleIds()) {
                Role role = roleRepository.findById(roleDto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

                // На всякий случай уберём дубликаты actionId
                Set<Integer> actionIds = new HashSet<>(roleDto.getActionIds());

                for (Integer actionId : actionIds) {
                    Action action = actionRepository.findById(actionId)
                            .orElseThrow(() -> new ResourceNotFoundException("Action not found"));

                    // 1) Ищем активную запись
                    Optional<UserSubServiceRoleAction> activeOpt =
                            repository.findByUserAndSubServiceAndRoleAndActionAndIsActiveIsFalse(user, subService, role, action);

                    if (activeOpt.isPresent()) {
                        UserSubServiceRoleAction existing = activeOpt.get();
                        if (!Boolean.TRUE.equals(existing.getIsActive())) {
                            existing.setIsActive(true);
                            toSaveOrUpdate.add(existing);
                        }
                        continue; // активная уже есть -> либо включили, либо пропустили
                    }

                    // 2) Активной нет — пробуем «восстановить» удалённую
                    Optional<UserSubServiceRoleAction> anyOpt =
                            repository.findTopByUserAndSubServiceAndRoleAndActionOrderByIdDesc(user, subService, role, action);

                    if (anyOpt.isPresent()) {
                        UserSubServiceRoleAction resurrect = anyOpt.get();
                        if (resurrect.getDeletedAt() != null) {
                            resurrect.setDeletedAt(null);   // восстановили
                        }
                        resurrect.setIsActive(true);        // и активировали
                        toSaveOrUpdate.add(resurrect);
                        continue;
                    }

                    // 3) Совсем нет — создаём новую
                    UserSubServiceRoleAction entity = new UserSubServiceRoleAction();
                    entity.setUser(user);
                    entity.setSubService(subService);
                    entity.setRole(role);
                    entity.setAction(action);
                    entity.setIsActive(true);
                    toSaveOrUpdate.add(entity);
                }
            }
        }

        if (!toSaveOrUpdate.isEmpty()) {
            repository.saveAll(toSaveOrUpdate);
        }
    }


    @Transactional
    @Override
    public void update(UUID userId, UserSubServiceRoleActionCreateDto dto) {
//        repository.deleteByUserId(userId);
        createIfNotExist(userId, dto);
    }

    @Override
    public UserSubServiceRoleActionDto getByUserId(UUID userId) {
        List<UserSubServiceRoleAction> entities = repository.findByUserId(userId);
        UserDto userDto = userMapper.toDto(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        return mapper.toDtoByUser(entities, userDto);
    }
    

    public Page<UserSubServiceRoleActionDto> filter(UserSubServiceRoleActionSearchDto searchDto, Pageable pageable) {
        Specification<UserSubServiceRoleAction> spec = Specification.where(null);
        if (searchDto.getActionId() != null) {
            spec = spec.and(UserSubServiceRoleActionSpec.hasActionId(searchDto.getActionId()));
        }
        if (searchDto.getUserId() != null) {
            spec = spec.and(UserSubServiceRoleActionSpec.hasUserId(searchDto.getUserId()));
        }
        if (searchDto.getRoleId() != null) {
            spec = spec.and(UserSubServiceRoleActionSpec.haseRoleId(searchDto.getRoleId()));
        }
        if (searchDto.getSubServiceId() != null) {
            spec = spec.and(UserSubServiceRoleActionSpec.hasSubServiceId(searchDto.getSubServiceId()));
        }
        return repository.findAll(spec, pageable)
                .map(mapper2::toDto);
    }

    @Transactional
    public PrivilegeActivationResponseDto activateOrDeactivate(PrivilegeActivationRequestDto request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean active = request.getActionType() == PrivilegeActivationRequestDto.ActionType.ACTIVATE;

        int affected;

        switch (request.getModelType()) {
            case SUB_SERVICE -> {
                SubService sub = subServiceRepository.findById(request.getModelId())
                        .orElseThrow(() -> new ResourceNotFoundException("SubService not found"));
                affected = repository.bulkSetActiveByUserAndSubService(user.getId(), sub.getId(), active);
            }
            case ROLE -> {
                Role role = roleRepository.findById(request.getModelId())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
                affected = repository.bulkSetActiveByUserAndRole(user.getId(), role.getId(), active);
            }
            default -> throw new IllegalArgumentException("Unsupported modelType");
        }
        return new PrivilegeActivationResponseDto(affected);
    }
}