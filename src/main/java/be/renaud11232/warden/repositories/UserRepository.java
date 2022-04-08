package be.renaud11232.warden.repositories;

import be.renaud11232.warden.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUuid(String uuid);

}
