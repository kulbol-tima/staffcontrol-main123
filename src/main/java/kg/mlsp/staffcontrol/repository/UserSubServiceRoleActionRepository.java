package kg.mlsp.staffcontrol.repository;

import kg.mlsp.staffcontrol.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSubServiceRoleActionRepository extends JpaRepository<UserSubServiceRoleAction, Integer>, JpaSpecificationExecutor<UserSubServiceRoleAction> {
    List<UserSubServiceRoleAction> findByUserId(UUID userId);
    Optional<UserSubServiceRoleAction> findByUserAndSubServiceAndRoleAndActionAndIsActiveIsFalse(
            User user, SubService subService, Role role, Action action
    );

    Optional<UserSubServiceRoleAction> findTopByUserAndSubServiceAndRoleAndActionOrderByIdDesc(
            User user, SubService subService, Role role, Action action
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value =
            // language=PostgreSQL
            """
            UPDATE user_sub_service_role_actions
               SET is_active = :active, updated_at = now()
             WHERE deleted_at IS NULL
               AND user_id = :userId
               AND sub_service_id = :subServiceId
            """, nativeQuery = true)
    int bulkSetActiveByUserAndSubService(@Param("userId") UUID userId,
                                         @Param("subServiceId") Integer subServiceId,
                                         @Param("active") boolean active);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value =
            // language=PostgreSQL
            """
            UPDATE user_sub_service_role_actions
               SET is_active = :active, updated_at = now()
             WHERE deleted_at IS NULL
               AND user_id = :userId
               AND role_id = :roleId
            """, nativeQuery = true)
    int bulkSetActiveByUserAndRole(@Param("userId") UUID userId,
                                   @Param("roleId") Integer roleId,
                                   @Param("active") boolean active);
}