package pe.upc.pescagobackend.iam.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.upc.pescagobackend.iam.domain.model.aggregates.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>findById(Long id);

    Optional<User> findByEmailAndPassword(String email, String password);
}
