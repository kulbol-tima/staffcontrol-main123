package kg.mlsp.staffcontrol.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class AccessPolicyAspect {

    @Before("@annotation(policy)")
    public void checkAccess(JoinPoint jp, AccessPolicy policy) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("⛔ Неавторизован");
        }

        if (!(auth.getPrincipal() instanceof UserPrincipal principal)) {
            throw new AccessDeniedException("⛔ Неверный тип пользователя");
        }

        if (principal.mustChangePassword() && !Objects.equals(jp.getSignature().getName(), "changePassword")) {
            throw new AccessDeniedException("⛔ Необходимо сменить пароль");
        }
        if (policy.roles().length == 0) {
            throw new AccessDeniedException("⛔ Роли не указаны в политике доступа");
        }

        boolean hasRole = Arrays.stream(policy.roles())
                .anyMatch(role -> principal.getRoles().contains(role));

        if (Arrays.stream(policy.roles()).anyMatch(role -> role.equals("ALL"))) {
            hasRole = true;
        }

        if (!hasRole) {
            if (Arrays.stream(policy.roles()).noneMatch(role -> role.equals("ALL"))) {
                throw new AccessDeniedException("⛔ Роль не разрешена");
            }
        }

        if (policy.organizations().length > 0) {
            boolean ok = false;
            if (principal.getOrganizationCode() != null) {
                ok = Arrays.asList(policy.organizations()).contains(principal.getOrganizationCode());
            }
            if (!ok) throw new AccessDeniedException("⛔ Организация запрещена");
        }

        if (policy.positions().length > 0) {
            boolean ok = false;
            if (principal.getPositionCode() != null) {
                ok = Arrays.asList(policy.positions()).contains(principal.getPositionCode());
            }
            if (!ok) throw new AccessDeniedException("⛔ Позиция не разрешена");
        }
    }
}
