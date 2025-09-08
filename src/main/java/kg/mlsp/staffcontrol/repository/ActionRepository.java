package kg.mlsp.staffcontrol.repository;

import kg.mlsp.staffcontrol.model.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ActionRepository extends JpaRepository<Action, Integer>, JpaSpecificationExecutor<Action> {
    @Query("SELECT a FROM Action a  WHERE a.role.id = :roleId")
    Page<Action> findByRoleId(@Param("roleId") Integer roleId, Pageable pageable);
    
    @Query("SELECT a FROM Action a WHERE " +
           "(:nameRu IS NULL OR LOWER(a.nameRu) LIKE LOWER(CONCAT('%', :nameRu, '%'))) AND " +
           "(:nameKy IS NULL OR LOWER(a.nameKy) LIKE LOWER(CONCAT('%', :nameKy, '%'))) AND " +
           "(:code IS NULL OR LOWER(a.code) LIKE LOWER(CONCAT('%', :code, '%')))")
    Page<Action> findByTextCriteria(
            @Param("nameRu") String nameRu,
            @Param("nameKy") String nameKy,
            @Param("code") String code,
            Pageable pageable
    );

    @Query("SELECT a FROM Action a WHERE " +
           "(:createdAtFrom IS NULL OR a.createdAt >= :createdAtFrom) AND " +
           "(:createdAtTo IS NULL OR a.createdAt <= :createdAtTo) AND " +
           "(:updatedAtFrom IS NULL OR a.updatedAt >= :updatedAtFrom) AND " +
           "(:updatedAtTo IS NULL OR a.updatedAt <= :updatedAtTo)")
    Page<Action> findByDateCriteria(
            @Param("createdAtFrom") LocalDateTime createdAtFrom,
            @Param("createdAtTo") LocalDateTime createdAtTo,
            @Param("updatedAtFrom") LocalDateTime updatedAtFrom,
            @Param("updatedAtTo") LocalDateTime updatedAtTo,
            Pageable pageable
    );
}