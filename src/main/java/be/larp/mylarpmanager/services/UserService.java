package be.larp.mylarpmanager.services;

import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.uuid.Skill;
import be.larp.mylarpmanager.models.uuid.User;
import be.larp.mylarpmanager.repositories.SkillRepository;
import be.larp.mylarpmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("User with uuid " + uuid + " not found."));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User with email " + email + " not found."));
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public boolean userWithEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean userWithUsername(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    public void save(User user) {
        userRepository.saveAndFlush(user);
    }
}
