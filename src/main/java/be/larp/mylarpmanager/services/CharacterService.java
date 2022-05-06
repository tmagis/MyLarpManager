package be.larp.mylarpmanager.services;

import be.larp.mylarpmanager.models.uuid.Character;
import be.larp.mylarpmanager.repositories.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public Character getCharacterByUuid(String uuid){
        return characterRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Character with uuid " + uuid + " not found."));
    }

    public void save(Character character) {
        characterRepository.saveAndFlush(character);
    }

    public void delete(Character character) {
        characterRepository.delete(character);
    }
}
