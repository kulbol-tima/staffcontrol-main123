package kg.mlsp.staffcontrol.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kg.mlsp.staffcontrol.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
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

}
