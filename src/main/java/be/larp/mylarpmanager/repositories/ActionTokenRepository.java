package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.ActionToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActionTokenRepository extends JpaRepository<ActionToken, Long> {
    Optional<ActionToken> findByToken(String token);
}
