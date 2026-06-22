package pe.upc.pescagobackend.shared.infrastructure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.upc.pescagobackend.shared.domain.model.enums.Role;

import java.util.Collection;
import java.util.List;

public class AuthenticatedUser implements UserDetails {

    private final Long userId;
    private final String email;
    private final Role canonicalRole;
    private final String storedPassword;

    public AuthenticatedUser(Long userId, String email, Role canonicalRole, String storedPassword) {
        this.userId = userId;
        this.email = email;
        this.canonicalRole = canonicalRole;
        this.storedPassword = storedPassword;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Role getCanonicalRole() {
        return canonicalRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + canonicalRole.name()));
    }

    @Override
    public String getPassword() {
        return storedPassword;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
