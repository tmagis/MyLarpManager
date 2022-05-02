package be.larp.mylarpmanager.security.services;

import be.larp.mylarpmanager.models.ActionToken;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.ActionTokenRepository;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.models.ActionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Value("${warden.app.resetPasswordTokenValidityInMinutes:15}")
    private int resetPasswordTokenValidityInMinutes;
    @Value("${warden.app.activateAccountTokenValidityInMinutes:1440}")
    private int activateAccountTokenValidityInMinutes;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActionTokenRepository actionTokenRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            logger.info("Blocked request for IP " + ip);
            throw new RuntimeException("blocked");
        } else {
            logger.trace("Non blocked request for IP " + ip);
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found."));
    }

    @Transactional
    public UserDetails loadUserByUuid(String uuid) throws UsernameNotFoundException {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("User with uuid " + uuid + " not found."));
    }

    public String getCurrentToken(String userUuid) throws UsernameNotFoundException {
        return userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new UsernameNotFoundException("User with uuid " + userUuid + " not found.")).getCurrentToken();
    }

    public String createActionToken(User user, ActionType actionType) {
        String token = UUID.randomUUID().toString();
        ActionToken actionToken = new ActionToken();
        actionToken.setToken(token);
        actionToken.setUser(user);
        actionToken.setActionType(actionType);
        LocalDateTime localDateTime;
        switch (actionType) {
            case VERIFY_EMAIL:
                localDateTime = LocalDateTime.now().plusMinutes(activateAccountTokenValidityInMinutes);
                break;
            case PASSWORD_RESET:
                localDateTime = LocalDateTime.now().plusMinutes(resetPasswordTokenValidityInMinutes);
                break;
            default:
                localDateTime = LocalDateTime.now();
        }
        actionToken.setExpirationTime(localDateTime);
        actionTokenRepository.saveAndFlush(actionToken);
        return token;
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
