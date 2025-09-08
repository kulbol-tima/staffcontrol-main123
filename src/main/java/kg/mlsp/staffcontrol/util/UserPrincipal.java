package kg.mlsp.staffcontrol.util;

import kg.mlsp.staffcontrol.model.Role;
import kg.mlsp.staffcontrol.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final User user;
    private final boolean mustChangePassword;

    public UserPrincipal(User user) {
        this.user = user;
        this.mustChangePassword = user.isMustChangePassword();
    }

    @Override public String getUsername() { return user.getUsername(); }
    @Override public String getPassword() { return user.getPassword(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getIsActive());
    }

    public List<String> getRoles() {
        return user.getRoles().stream()
                .map(Role::getCode)
                .toList();
    }

    public String getOrganizationCode() {
        return user.getStaff() != null && user.getStaff().getOrganization() != null 
                ? user.getStaff().getOrganization().getCode() 
                : null;
    }

    public String getPositionCode() {
        return user.getStaff() != null && user.getStaff().getPosition() != null 
                ? user.getStaff().getPosition().getCode() 
                : null;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // You can add roles here
    }

    public boolean mustChangePassword() {
        return mustChangePassword;
    }

}

