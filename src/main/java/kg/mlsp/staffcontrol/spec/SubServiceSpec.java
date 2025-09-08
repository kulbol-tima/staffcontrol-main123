package kg.mlsp.staffcontrol.spec;

import kg.mlsp.staffcontrol.model.SubService;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class SubServiceSpec {
    public static Specification<SubService> hasNameRu(String nameRu) {
        return (root, query, criteriaBuilder) -> {
            if (nameRu == null || nameRu.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("nameRu")), "%" + nameRu.toLowerCase() + "%");
        };

    }
    public static Specification<SubService> hasNameKy(String nameKy) {
        return (root, query, criteriaBuilder) -> {
            if (nameKy == null || nameKy.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("nameKy")), "%" + nameKy.toLowerCase() + "%");
        };
    }

    public static Specification<SubService> hasCode(String code) {
        return (root, query, criteriaBuilder) -> {
            if (code == null || code.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + code.toLowerCase() + "%");
        };
    }

    public static Specification<SubService> hasIsActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<SubService> hasCreatedAtFrom(LocalDateTime createdAtFrom) {
        return (root, query, criteriaBuilder) -> {
            if (createdAtFrom == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAtFrom);
        };
    }
    public static Specification<SubService> hasCreatedAtTo(LocalDateTime createdAtTo) {
        return (root, query, criteriaBuilder) -> {
            if (createdAtTo == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdAtTo);
        };
    }
    public static Specification<SubService> hasUpdatedAtFrom(LocalDateTime updatedAtFrom) {
        return (root, query, criteriaBuilder) -> {
            if (updatedAtFrom == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), updatedAtFrom);
        };
    }
    public static Specification<SubService> hasUpdatedAtTo(LocalDateTime updatedAtTo) {
        return (root, query, criteriaBuilder) -> {
            if (updatedAtTo == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), updatedAtTo);
        };
    }
}
