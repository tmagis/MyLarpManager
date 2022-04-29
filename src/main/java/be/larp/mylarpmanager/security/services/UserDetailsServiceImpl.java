package be.larp.mylarpmanager.security.services;

import be.larp.mylarpmanager.controllers.Controller;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    UserRepository userRepository;

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
        }else{
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

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
