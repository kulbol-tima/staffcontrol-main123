package kg.mlsp.staffcontrol.spec;

import kg.mlsp.staffcontrol.model.Role;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class RoleSpec {
    public static Specification<Role> hasNameRu(String nameRu) {
        return (root, query, criteriaBuilder) -> {
            if (nameRu == null || nameRu.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("nameRu")), "%" + nameRu.toLowerCase() + "%");
        };

    }
    public static Specification<Role> hasNameKy(String nameKy) {
        return (root, query, criteriaBuilder) -> {
            if (nameKy == null || nameKy.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("nameKy")), "%" + nameKy.toLowerCase() + "%");
        };
    }

    public static Specification<Role> hasCode(String code) {
        return (root, query, criteriaBuilder) -> {
            if (code == null || code.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + code.toLowerCase() + "%");
        };
    }

    public static Specification<Role> hasSubServiceId(Integer subServiceId) {
        return (root, query, criteriaBuilder) -> {
            if (subServiceId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("subService").get("id"), subServiceId);
        };
    }

    public static Specification<Role> hasIsActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<Role> hasCreatedAtFrom(LocalDateTime createdAtFrom) {
        return (root, query, criteriaBuilder) -> {
            if (createdAtFrom == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAtFrom);
        };
    }
    public static Specification<Role> hasCreatedAtTo(LocalDateTime createdAtTo) {
        return (root, query, criteriaBuilder) -> {
            if (createdAtTo == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdAtTo);
        };
    }
    public static Specification<Role> hasUpdatedAtFrom(LocalDateTime updatedAtFrom) {
        return (root, query, criteriaBuilder) -> {
            if (updatedAtFrom == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), updatedAtFrom);
        };
    }
    public static Specification<Role> hasUpdatedAtTo(LocalDateTime updatedAtTo) {
        return (root, query, criteriaBuilder) -> {
            if (updatedAtTo == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), updatedAtTo);
        };
    }
}
