package be.larp.mylarpmanager.startup;

import be.larp.mylarpmanager.models.*;
import be.larp.mylarpmanager.models.uuid.Character;
import be.larp.mylarpmanager.models.uuid.Nation;
import be.larp.mylarpmanager.models.uuid.User;
import be.larp.mylarpmanager.repositories.CharacterRepository;
import be.larp.mylarpmanager.repositories.NationRepository;
import be.larp.mylarpmanager.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class FirstSetup implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(FirstSetup.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    NationRepository nationRepository;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() {
        if (userRepository.count() == 0) {
            Nation nation = new Nation()
                    .setName(
                            new TranslatedItem()
                                    .setEn("Sausage")
                                    .setFr("Saucisse")
                                    .setNl("Worst")
                    )
                    .setContributionMandatory(false)
                    .setInternationalFriendly(false)
                    .setFamilyFriendly(false);
            nationRepository.saveAndFlush(nation);
            String username = "admin";
            String password = "changeme";
            String email = "admin@admin.be";
            String firstName = "Gaspard";
            String lastName = "Délicieux";
            User defaultUser = new User()
                    .setRole(Role.ADMIN)
                    .setEnabled(true)
                    .setUsername(username)
                    .setPassword(passwordEncoder.encode(password))
                    .setEmail(email)
                    .setLastName(lastName)
                    .setFirstName(firstName)
                    .setNation(nation);
            userRepository.saveAndFlush(defaultUser);
            String chname = "Bobby";
            String background = "Once upon a time, me.";
            String race = "Half Human half stupid";
            Character character = new Character()
                    .setName(chname)
                    .setBackground(background)
                    .setRace(race)
                    .setAlive(true)
                    .setCreationTime(LocalDateTime.now())
                    .setLastModificationTime(LocalDateTime.now())
                    .setPlayer(defaultUser);
            characterRepository.saveAndFlush(character);
            logger.info("============================================================================================================");
            logger.info(" \\ \\        / /          | |");
            logger.info("  \\ \\  /\\  / /_ _ _ __ __| | ___ _ __");
            logger.info("   \\ \\/  \\/ / _` | '__/ _` |/ _ \\ '_ \\");
            logger.info("    \\  /\\  / (_| | | | (_| |  __/ | | |");
            logger.info("     \\/  \\/ \\__,_|_|  \\__,_|\\___|_| |_|");
            logger.info("============================================================================================================");
            logger.info("Welcome, the default user has been created. You can now login to the webUI using the following credentials :");
            logger.info("Username : " + username);
            logger.info("Password : " + password);
            logger.info("Once connected, don't forget to change these default credentials");
            logger.info("============================================================================================================");
        }
    }
}
