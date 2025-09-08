package kg.mlsp.staffcontrol.spec;

import kg.mlsp.staffcontrol.model.Action;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ActionSpec {
    public static Specification<Action> hasNameRu(String nameRu) {
        return (root, query, criteriaBuilder) -> {
            if (nameRu == null || nameRu.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("nameRu")), "%" + nameRu.toLowerCase() + "%");
        };

    }
    public static Specification<Action> hasNameKy(String nameKy) {
        return (root, query, criteriaBuilder) -> {
            if (nameKy == null || nameKy.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("nameKy")), "%" + nameKy.toLowerCase() + "%");
        };
    }

    public static Specification<Action> hasCode(String code) {
        return (root, query, criteriaBuilder) -> {
            if (code == null || code.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + code.toLowerCase() + "%");
        };
    }
    public static Specification<Action> hasCreatedAtFrom(LocalDateTime createdAtFrom) {
        return (root, query, criteriaBuilder) -> {
            if (createdAtFrom == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAtFrom);
        };
    }
    public static Specification<Action> hasCreatedAtTo(LocalDateTime createdAtTo) {
        return (root, query, criteriaBuilder) -> {
            if (createdAtTo == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdAtTo);
        };
    }
    public static Specification<Action> hasUpdatedAtFrom(LocalDateTime updatedAtFrom) {
        return (root, query, criteriaBuilder) -> {
            if (updatedAtFrom == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), updatedAtFrom);
        };
    }
    public static Specification<Action> hasUpdatedAtTo(LocalDateTime updatedAtTo) {
        return (root, query, criteriaBuilder) -> {
            if (updatedAtTo == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), updatedAtTo);
        };
    }
    public static Specification<Action> hasRoleId(Integer roleId) {
        return (root, query, criteriaBuilder) -> {
            if (roleId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("role").get("id"), roleId);
        };
    }
}
