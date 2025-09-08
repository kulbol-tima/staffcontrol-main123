package kg.mlsp.staffcontrol.spec;

import kg.mlsp.staffcontrol.model.UserSubServiceRoleAction;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class UserSubServiceRoleActionSpec {
    public static Specification<UserSubServiceRoleAction> hasActionId(Integer actionId) {
        return (root, query, criteriaBuilder) -> {
            if (actionId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("action").get("id"), actionId);
        };
    }

    public  static Specification<UserSubServiceRoleAction> haseRoleId(Integer roleId) {
        return (root, query, criteriaBuilder) -> {
            if (roleId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("role").get("id"), roleId);
        };
    }

    public static Specification<UserSubServiceRoleAction> hasSubServiceId(Integer subServiceId) {
        return (root, query, criteriaBuilder) -> {
            if (subServiceId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("subservice").get("id"), subServiceId);
        };
    }

    public static Specification<UserSubServiceRoleAction> hasUserId(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }
}

