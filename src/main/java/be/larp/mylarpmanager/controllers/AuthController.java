package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.exceptions.ExpiredTokenException;
import be.larp.mylarpmanager.models.ActionToken;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.ActionTokenRepository;
import be.larp.mylarpmanager.requests.ChangePasswordRequest;
import be.larp.mylarpmanager.requests.LoginRequest;
import be.larp.mylarpmanager.requests.ResetPasswordRequest;
import be.larp.mylarpmanager.responses.Token;
import be.larp.mylarpmanager.security.events.OnPasswordResetEvent;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController extends Controller {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    ActionTokenRepository actionTokenRepository;

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

    @PostMapping("/resetpassword")
    public ResponseEntity<?> resetPassword(HttpServletRequest request, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User with email " + resetPasswordRequest.getEmail() + " not found."));
        //TODO Improve
        String host = String.valueOf(request.getRequestURL().delete(request.getRequestURL().indexOf(request.getRequestURI()), request.getRequestURL().length()));
        eventPublisher.publishEvent(new OnPasswordResetEvent(user,host , request.getLocale()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/signoff")
    public ResponseEntity<?> signOff() {
        User requester = getRequestUser();
        requester.setCurrentToken(null);
        userRepository.saveAndFlush(requester);
        trace(requester, "signed off.", null);
        return ResponseEntity.ok().build();
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
            return ResponseEntity.ok().build();
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


    @GetMapping("/confirm")
    public ResponseEntity<?> confirmRegistration
            (WebRequest request, @RequestParam("token") String token) {
        Locale locale = request.getLocale();
        ActionToken actionToken = actionTokenRepository.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("Token is invalid."));
        if(LocalDateTime.now().isAfter(actionToken.getExpirationTime())){
            throw new ExpiredTokenException("The token is expired");
        }
        User user = actionToken.getUser();
        user.setEnabled(true);
        userRepository.saveAndFlush(user);
        trace(user, "activated account", null);
        return ResponseEntity.ok().build();
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
