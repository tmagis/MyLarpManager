package be.renaud11232.warden.security.services;

import be.renaud11232.warden.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found."));
    }

    @Transactional
    public UserDetails loadUserByUuid(String uuid) throws UsernameNotFoundException {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("User with uuid " + uuid + " not found."));
    }
}
