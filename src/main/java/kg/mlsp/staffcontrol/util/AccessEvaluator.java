package kg.mlsp.staffcontrol.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component("accessEvaluator")
public class AccessEvaluator {

    public boolean check(Authentication auth,
                         String[] requiredRoles,
                         String[] requiredOrgs,
                         String[] requiredPositions) {
        if (auth == null || !auth.isAuthenticated()) return false;

        var authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        for (String role : requiredRoles) {
            if (!authorities.contains("ROLE_" + role)) return false;
        }

        if (auth.getPrincipal() instanceof UserPrincipal user) {
            if (requiredOrgs.length > 0 && Arrays.stream(requiredOrgs).noneMatch(o -> o.equals(user.getOrganizationCode())))
                return false;

            if (requiredPositions.length > 0 && Arrays.stream(requiredPositions).noneMatch(p -> p.equals(user.getPositionCode())))
                return false;
        }

        return true;
    }
}

