package kg.mlsp.staffcontrol.service;

import org.springframework.transaction.annotation.Transactional;
import kg.mlsp.staffcontrol.dto.UserCreateDto;
import kg.mlsp.staffcontrol.dto.UserDto;
import kg.mlsp.staffcontrol.dto.UserSearchDto;
import kg.mlsp.staffcontrol.dto.UserUpdateDto;
import kg.mlsp.staffcontrol.dto.request.UserResetCredentialsDto;
import kg.mlsp.staffcontrol.exception.ResourceNotFoundException;
import kg.mlsp.staffcontrol.mapper.UserMapper;
import kg.mlsp.staffcontrol.model.Organization;
import kg.mlsp.staffcontrol.model.Position;
import kg.mlsp.staffcontrol.model.Staff;
import kg.mlsp.staffcontrol.model.User;
import kg.mlsp.staffcontrol.repository.OrganizationRepository;
import kg.mlsp.staffcontrol.repository.PositionRepository;
import kg.mlsp.staffcontrol.repository.StaffRepository;
import kg.mlsp.staffcontrol.repository.UserRepository;
import kg.mlsp.staffcontrol.util.PasswordValidator;
import kg.mlsp.staffcontrol.util.SecurityUtils;
import kg.mlsp.staffcontrol.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final OrganizationRepository organizationRepository;
    private final PositionRepository positionRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final LoggingService loggingService;

    @Transactional(readOnly = true)
    public Page<UserDto> getAll(Pageable pageable) {
        // Проверяем, есть ли сортировка по полям Staff
        if (pageable.getSort().isSorted()) {
            return getSortedUsers(pageable);
        }
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> searchUsers(UserSearchDto searchDto, Pageable pageable) {
        // Проверяем, есть ли текстовые критерии поиска
        boolean hasTextCriteria = (searchDto.getFirstName() != null && !searchDto.getFirstName().isEmpty()) ||
                                 (searchDto.getLastName() != null && !searchDto.getLastName().isEmpty()) ||
                                 (searchDto.getMiddleName() != null && !searchDto.getMiddleName().isEmpty()) ||
                                 (searchDto.getPhone() != null && !searchDto.getPhone().isEmpty()) ||
                                 (searchDto.getPin() != null && !searchDto.getPin().isEmpty());

        // Проверяем, есть ли критерии по датам
        boolean hasDateCriteria = searchDto.getCreatedAtFrom() != null ||
                                 searchDto.getUpdatedAtTo() != null ||
                                 searchDto.getUpdatedAtFrom() != null ||
                                 searchDto.getUpdatedAtTo() != null;

        if (hasTextCriteria && hasDateCriteria) {
            // Если есть и текстовые, и дата критерии, используем базовый поиск
            return userRepository.findAll(pageable).map(userMapper::toDto);
        } else if (hasTextCriteria) {
            // Только текстовый поиск
            return userRepository.findByTextCriteria(
                    searchDto.getFirstName(),
                    searchDto.getLastName(),
                    searchDto.getMiddleName(),
                    searchDto.getPhone(),
                    searchDto.getPin(),
                    pageable
            ).map(userMapper::toDto);
        } else if (hasDateCriteria) {
            // Только поиск по датам
            return userRepository.findByDateCriteria(
                    searchDto.getCreatedAtFrom(),
                    searchDto.getUpdatedAtTo(),
                    searchDto.getUpdatedAtFrom(),
                    searchDto.getUpdatedAtTo(),
                    pageable
            ).map(userMapper::toDto);
        } else {
            // Нет критериев - возвращаем все
            return userRepository.findAll(pageable).map(userMapper::toDto);
        }
    }

    @Transactional(readOnly = true)
    public UserDto getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto createUser(UserCreateDto dto) {
        userValidator.userCreateValidate(dto);
        Organization org = null;
        if (dto.getOrganizationId() != null) {
            org = organizationRepository.findById(dto.getOrganizationId()).orElse(null);
        }

        Position pos = null;
        if (dto.getPositionId() != null) {
            pos = positionRepository.findById(dto.getPositionId()).orElse(null);
        }

        // Создаём Staff
        Staff staff = new Staff();
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setMiddleName(dto.getMiddleName());
        staff.setBirthDate(dto.getBirthDate());
        staff.setPhone(dto.getPhone());
        staff.setOrganization(org);
        staff.setPosition(pos);
        staff = staffRepository.save(staff);

        // Создаём User
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setStaff(staff);
        user.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        user = userRepository.save(user);
        UserDto result = userMapper.toDto(user);
        
        // Логируем создание
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logCreate("User", user.getId().toString(), result, null, username);
        
        return result;
    }

    @Transactional
    public UserDto updateUser(UUID id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Сохраняем старые данные для логирования
        UserDto oldData = userMapper.toDto(user);

        Staff staff = user.getStaff();

        // Обновляем STAFF
        staff.setPhone(dto.getPhone());

        if (dto.getOrganizationId() != null) {
            Organization org = organizationRepository.findById(dto.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
            staff.setOrganization(org);
        }

        if (dto.getPositionId() != null) {
            Position pos = positionRepository.findById(dto.getPositionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Position not found"));
            staff.setPosition(pos);
        }

        staffRepository.save(staff);

        user.setEmail(dto.getEmail());

        if (dto.getIsActive() != null) {
            user.setIsActive(dto.getIsActive());
        }

        user = userRepository.save(user);
        UserDto newData = userMapper.toDto(user);
        
        // Логируем обновление
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logUpdate("User", user.getId().toString(), oldData, newData, null, username);
        
        return newData;
    }


    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Сохраняем данные для логирования
        UserDto oldData = userMapper.toDto(user);

        // Удаляем связанный staff
        if (user.getStaff() != null) {
            staffRepository.deleteById(user.getStaff().getId());
        }

        userRepository.delete(user);
        
        // Логируем удаление
        String username = SecurityUtils.getCurrentUsername().orElse("system");
        loggingService.logDelete("User", user.getId().toString(), oldData, null, username);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        if (!PasswordValidator.isStrong(newPassword)) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 8 символов, строчные/ЗАГЛАВНЫЕ буквы, цифру и спецсимвол");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Старый пароль неверен");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        userRepository.save(user);
    }

    @Transactional
    public UserDto resetUserCredentials(UUID id, UserResetCredentialsDto dto) {
        // Проверяем силу нового пароля
        if (!PasswordValidator.isStrong(dto.getNewPassword())) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 8 символов, строчные/ЗАГЛАВНЫЕ буквы, цифру и спецсимвол");
        }

        // Находим пользователя
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Проверяем, что новый логин не занят другим пользователем
        if (!user.getUsername().equals(dto.getNewUsername())) {
            if (userRepository.existsByUsername(dto.getNewUsername())) {
                throw new IllegalArgumentException("Логин '" + dto.getNewUsername() + "' уже занят");
            }
        }

        // Обновляем логин и пароль
        user.setUsername(dto.getNewUsername());
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        
        // Устанавливаем флаг обязательной смены пароля
        user.setMustChangePassword(true);
        
        // Сохраняем изменения
        user = userRepository.save(user);
        
        return userMapper.toDto(user);
    }

    private Page<UserDto> getSortedUsers(Pageable pageable) {
        // Получаем первую сортировку (если есть несколько)
        var sort = pageable.getSort().get().findFirst();
        
        if (sort.isPresent()) {
            String property = sort.get().getProperty();
            boolean isAscending = sort.get().getDirection().isAscending();
            
            return switch (property) {
                case "staff.firstName" -> isAscending ? 
                    userRepository.findAllOrderByStaffFirstNameAsc(pageable).map(userMapper::toDto) :
                    userRepository.findAllOrderByStaffFirstNameDesc(pageable).map(userMapper::toDto);
                case "staff.lastName" -> isAscending ? 
                    userRepository.findAllOrderByStaffLastNameAsc(pageable).map(userMapper::toDto) :
                    userRepository.findAllOrderByStaffLastNameDesc(pageable).map(userMapper::toDto);
                case "staff.middleName" -> isAscending ? 
                    userRepository.findAllOrderByStaffMiddleNameAsc(pageable).map(userMapper::toDto) :
                    userRepository.findAllOrderByStaffMiddleNameDesc(pageable).map(userMapper::toDto);
                case "staff.phone" -> isAscending ? 
                    userRepository.findAllOrderByStaffPhoneAsc(pageable).map(userMapper::toDto) :
                    userRepository.findAllOrderByStaffPhoneDesc(pageable).map(userMapper::toDto);
                case "staff.pin" -> isAscending ? 
                    userRepository.findAllOrderByStaffPinAsc(pageable).map(userMapper::toDto) :
                    userRepository.findAllOrderByStaffPinDesc(pageable).map(userMapper::toDto);
                default -> userRepository.findAll(pageable).map(userMapper::toDto);
            };
        }
        
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

}
