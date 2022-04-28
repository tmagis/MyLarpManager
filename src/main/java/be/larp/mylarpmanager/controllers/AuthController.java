package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.requests.ChangePasswordRequest;
import be.larp.mylarpmanager.requests.LoginRequest;
import be.larp.mylarpmanager.responses.Token;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController extends Controller {

    Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = createToken(authentication, getRequestUser());
        trace(getRequestUser(), "successful login.", null);
        return ResponseEntity.ok(new Token(jwt));
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> reset(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        User user = getRequestUser();
        if (encoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getNewPasswordConfirmation())) {
                user.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
                userRepository.saveAndFlush(user);
                trace(user, "password change.", null);
                return ResponseEntity.ok().build();
            } else {
                throw new BadRequestException("The two passwords don't match.");
            }
        } else {
            throw new BadCredentialsException("The current password is not valid.");
        }
    }

    @GetMapping("/signoff")
    public ResponseEntity<?> signOff() {
        User requester = getRequestUser();
        requester.setCurrentToken(null);
        userRepository.saveAndFlush(requester);
        trace(requester, "signed off.", null);
        return ResponseEntity.ok(ResponseEntity.ok());
    }

    @GetMapping("/massivesignoff")
    public ResponseEntity<?> massiveSignOff() {
        User requester = getRequestUser();
        if (requester.isAdmin()) {
            userRepository.findAll().forEach(u -> {
                u.setCurrentToken(null);
                userRepository.saveAndFlush(u);
            });
            trace(requester, "signed everybody off.", null);
            return ResponseEntity.ok(ResponseEntity.ok());
        } else {
            throw new BadPrivilegesException();
        }
    }

    @GetMapping("/whoami")
    public ResponseEntity<?> whoAmI() {
        User requester = getRequestUser();
        trace(requester, "load his details.", null);
        return ResponseEntity.ok(requester);
    }

    @GetMapping("/renew")
    public ResponseEntity<?> renew() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User requester = getRequestUser();
        String jwt = createToken(authentication, requester);
        trace(getRequestUser(), "renew his JWT Token.", null);
        return ResponseEntity.ok(new Token(jwt));
    }

    private String createToken(Authentication authentication, User requester) {
        String jwt = jwtUtils.generateJwtToken(authentication);
        requester.setCurrentToken(encoder.encode(jwt));
        userRepository.saveAndFlush(requester);
        return jwt;
    }


}
