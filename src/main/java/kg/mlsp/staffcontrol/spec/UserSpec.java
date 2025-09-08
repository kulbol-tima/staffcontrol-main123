package kg.mlsp.staffcontrol.spec;

import kg.mlsp.staffcontrol.dto.UserSearchDto;
import kg.mlsp.staffcontrol.model.Staff;
import kg.mlsp.staffcontrol.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSpec {

    public Specification<User> getSpec(UserSearchDto searchDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchDto != null) {
                if (searchDto.getFirstName() != null && !searchDto.getFirstName().isEmpty()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("staff").get("firstName")), "%" + searchDto.getFirstName().toLowerCase() + "%"));
                }
                if (searchDto.getLastName() != null && !searchDto.getLastName().isEmpty()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("staff").get("lastName")), "%" + searchDto.getLastName().toLowerCase() + "%"));
                }
                if (searchDto.getMiddleName() != null && !searchDto.getMiddleName().isEmpty()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("staff").get("middleName")), "%" + searchDto.getMiddleName().toLowerCase() + "%"));
                }
                if (searchDto.getPhone() != null && !searchDto.getPhone().isEmpty()) {
                    predicates.add(criteriaBuilder.like(root.get("staff").get("phone"), "%" + searchDto.getPhone() + "%"));
                }
                if (searchDto.getPin() != null && !searchDto.getPin().isEmpty()) {
                    predicates.add(criteriaBuilder.like(root.get("staff").get("pin"), "%" + searchDto.getPin() + "%"));
                }
                if (searchDto.getCreatedAtFrom() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), searchDto.getCreatedAtFrom()));
                }
                if (searchDto.getCreatedAtTo() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), searchDto.getCreatedAtTo()));
                }
                if (searchDto.getUpdatedAtFrom() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), searchDto.getUpdatedAtFrom()));
                }
                if (searchDto.getUpdatedAtTo() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), searchDto.getUpdatedAtTo()));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
