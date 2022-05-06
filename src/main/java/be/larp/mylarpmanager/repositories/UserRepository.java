package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.uuid.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UuidRepository<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
