package kg.mlsp.staffcontrol.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kg.mlsp.staffcontrol.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Override
    Page<User> findAll(Pageable pageable);

    boolean existsByUsername(@NotBlank @Size(min = 3, max = 100) String username);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    Optional<User> findById(UUID id);

    void deleteById(UUID id);

    boolean existsByEmail(@NotBlank @Size(min = 3, max = 100) String email);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u JOIN u.staff s WHERE " +
           "(:firstName IS NULL OR LOWER(s.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:middleName IS NULL OR LOWER(s.middleName) LIKE LOWER(CONCAT('%', :middleName, '%'))) AND " +
           "(:phone IS NULL OR LOWER(s.phone) LIKE LOWER(CONCAT('%', :phone, '%'))) AND " +
           "(:pin IS NULL OR LOWER(s.pin) LIKE LOWER(CONCAT('%', :pin, '%')))")
    Page<User> findByTextCriteria(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("middleName") String middleName,
            @Param("phone") String phone,
            @Param("pin") String pin,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u WHERE " +
           "(:createdAtFrom IS NULL OR u.createdAt >= :createdAtFrom) AND " +
           "(:createdAtTo IS NULL OR u.createdAt <= :createdAtTo) AND " +
           "(:updatedAtFrom IS NULL OR u.updatedAt >= :updatedAtFrom) AND " +
           "(:updatedAtTo IS NULL OR u.updatedAt <= :updatedAtTo)")
    Page<User> findByDateCriteria(
            @Param("createdAtFrom") LocalDateTime createdAtFrom,
            @Param("createdAtTo") LocalDateTime createdAtTo,
            @Param("updatedAtFrom") LocalDateTime updatedAtFrom,
            @Param("updatedAtTo") LocalDateTime updatedAtTo,
            Pageable pageable
    );

    // Методы для сортировки по полям Staff
    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u JOIN u.staff s ORDER BY s.firstName ASC")
    Page<User> findAllOrderByStaffFirstNameAsc(Pageable pageable);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u JOIN u.staff s ORDER BY s.firstName DESC")
    Page<User> findAllOrderByStaffFirstNameDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u JOIN u.staff s ORDER BY s.lastName ASC")
    Page<User> findAllOrderByStaffLastNameAsc(Pageable pageable);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u JOIN u.staff s ORDER BY s.lastName DESC")
    Page<User> findAllOrderByStaffLastNameDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u JOIN u.staff s ORDER BY s.middleName ASC")
    Page<User> findAllOrderByStaffMiddleNameAsc(Pageable pageable);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u JOIN u.staff s ORDER BY s.middleName DESC")
    Page<User> findAllOrderByStaffMiddleNameDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u JOIN u.staff s ORDER BY s.phone ASC")
    Page<User> findAllOrderByStaffPhoneAsc(Pageable pageable);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u JOIN u.staff s ORDER BY s.phone DESC")
    Page<User> findAllOrderByStaffPhoneDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u JOIN u.staff s ORDER BY s.pin ASC")
    Page<User> findAllOrderByStaffPinAsc(Pageable pageable);

    @EntityGraph(attributePaths = {"staff", "userSubServiceRoleActions", "userSubServiceRoleActions.role", "userSubServiceRoleActions.subService"})
    @Query("SELECT u FROM User u JOIN u.staff s ORDER BY s.pin DESC")
    Page<User> findAllOrderByStaffPinDesc(Pageable pageable);
}
