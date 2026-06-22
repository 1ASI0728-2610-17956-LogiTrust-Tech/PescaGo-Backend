package pe.upc.pescagobackend.iam.application.internal.authenticationservices;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Optional;

@Service
public class PasswordAuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordAuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Optional<User> authenticate(String email, String rawPassword) {
        if (email == null || email.isBlank() || rawPassword == null) {
            return Optional.empty();
        }

        return userRepository.findByEmail(email.trim())
                .filter(user -> verifyAndUpgradePassword(user, rawPassword));
    }

    private boolean verifyAndUpgradePassword(User user, String rawPassword) {
        String storedPassword = user.getPassword();

        if (isBcryptHash(storedPassword)) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        if (!rawPassword.equals(storedPassword)) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
        return true;
    }

    public static boolean isBcryptHash(String value) {
        return value != null
                && (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"));
    }
}
