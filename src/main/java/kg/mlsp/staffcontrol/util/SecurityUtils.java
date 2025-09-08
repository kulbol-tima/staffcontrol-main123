package kg.mlsp.staffcontrol.util;

import kg.mlsp.staffcontrol.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtils {

    public static Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return Optional.of(userPrincipal.getUser());
        }
        return Optional.empty();
    }

    public static Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return Optional.of(((UserPrincipal) authentication.getPrincipal()).getUsername());
        }
        return Optional.empty();
    }

    public static Optional<String> getCurrentUserId() {
        return getCurrentUser().map(user -> user.getId().toString());
    }
}
