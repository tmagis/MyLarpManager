package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.uuid.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long>, UuidRepository<Character> {

}
