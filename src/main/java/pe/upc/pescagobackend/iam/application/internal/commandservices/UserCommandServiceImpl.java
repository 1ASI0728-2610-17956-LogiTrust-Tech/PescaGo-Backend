package pe.upc.pescagobackend.iam.application.internal.commandservices;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.domain.model.commands.CreateUserCommand;
import pe.upc.pescagobackend.iam.domain.model.commands.DeleteUserCommand;
import pe.upc.pescagobackend.iam.domain.services.UserCommandService;
import pe.upc.pescagobackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCommandServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> handle(CreateUserCommand command) {
        var user = new User(command);
        user.setPassword(passwordEncoder.encode(command.password()));
        try {
            userRepository.save(user);
            return Optional.of(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving the user: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteUserCommand command) {
       if (!userRepository.existsById(command.userId())) {
            throw new IllegalArgumentException("User with id %s not found".formatted(command.userId()));
        }
        try {
            userRepository.deleteById(command.userId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting user: %s".formatted(e.getMessage()));
        }
    }
}
