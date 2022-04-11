package be.larp.mylarpmanager.startup;

import be.larp.mylarpmanager.models.Role;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FirstSetup implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(FirstSetup.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(userRepository.count() == 0) {
            String username = "admin";
            String password = "changeme";
            String email = "admin@admin.be";
            String firstName = "Gaspard";
            String lastName = "Délicieux";
            User defaultUser = new User();
            defaultUser.setRole(Role.ADMIN);
            defaultUser.setUsername(username);
            defaultUser.setPassword(passwordEncoder.encode(password));
            defaultUser.setEmail(email);
            defaultUser.setLastName(lastName);
            defaultUser.setFirstName(firstName);
            defaultUser.setUuid(UUID.randomUUID().toString());
            userRepository.saveAndFlush(defaultUser);
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
