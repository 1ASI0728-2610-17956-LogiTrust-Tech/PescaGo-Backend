package pe.upc.pescagobackend.shared.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.upc.pescagobackend.shared.application.RoleCompatibilityMapper;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public SecurityUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new AuthenticatedUser(
                user.getId(),
                user.getEmail(),
                RoleCompatibilityMapper.toCanonicalRole(user.getRole()),
                user.getPassword()
        );
    }

    public UserDetails loadUserById(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new AuthenticatedUser(
                user.getId(),
                user.getEmail(),
                RoleCompatibilityMapper.toCanonicalRole(user.getRole()),
                user.getPassword()
        );
    }
}
