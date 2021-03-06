package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.exceptions.ExpiredTokenException;
import be.larp.mylarpmanager.models.ActionToken;
import be.larp.mylarpmanager.models.ActionType;
import be.larp.mylarpmanager.models.uuid.User;
import be.larp.mylarpmanager.repositories.ActionTokenRepository;
import be.larp.mylarpmanager.requests.ChangePasswordRequest;
import be.larp.mylarpmanager.requests.LoginRequest;
import be.larp.mylarpmanager.requests.ResetPasswordRequest;
import be.larp.mylarpmanager.requests.SetPasswordRequest;
import be.larp.mylarpmanager.responses.Token;
import be.larp.mylarpmanager.security.events.OnPasswordResetEvent;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController extends Controller {
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

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        User user = getRequestUser();
        user.setCurrentToken(null);
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> reset(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        User user = getRequestUser();
        if (encoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            changePassword(changePasswordRequest.getNewPassword(), changePasswordRequest.getNewPasswordConfirmation(), user);
            return ResponseEntity.ok().build();
        } else {
            throw new BadCredentialsException("The current password is not valid.");
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(HttpServletRequest request, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        User user = userService.getUserByEmail(resetPasswordRequest.getEmail());
        eventPublisher.publishEvent(new OnPasswordResetEvent(user, getHost(request), request.getLocale()));
        return ResponseEntity.ok().build();
    }

    private void changePassword(String password, String passwordConfirmation, User user) {
        checkSamePassword(password, passwordConfirmation);
        user.setPassword(encoder.encode(password));
        userService.save(user);
        trace(user, "password change.", null);
    }


    @PostMapping("/setPassword")
    public ResponseEntity<?> setPassword(@Valid @RequestBody SetPasswordRequest setPasswordRequest) {
        ActionToken actionToken = getActionToken(setPasswordRequest.getToken(), ActionType.PASSWORD_RESET);
        changePassword(setPasswordRequest.getNewPassword(), setPasswordRequest.getNewPasswordConfirmation(), actionToken.getUser());
        return ResponseEntity.ok().build();
    }

    private ActionToken getActionToken(String token, ActionType actionType) {
        ActionToken actionToken = actionTokenRepository.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("Token is invalid."));
        if (!actionToken.getActionType().equals(actionType)) {
            throw new BadRequestException("The token is not valid for this kind of action.");
        }
        if (LocalDateTime.now().isAfter(actionToken.getExpirationTime())) {
            throw new ExpiredTokenException("The token is expired.");
        }
        return actionToken;
    }

    @GetMapping("/signOff")
    public ResponseEntity<?> signOff() {
        User requester = getRequestUser();
        requester.setCurrentToken(null);
        userService.save(requester);
        trace(requester, "signed off.", null);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/massiveSignOff")
    public ResponseEntity<?> massiveSignOff() {
        User requester = getRequestUser();
        if (requester.isAdmin()) {
            userService.getAll().forEach(u -> {
                u.setCurrentToken(null);
                userService.save(u);
            });
            trace(requester, "signed everybody off.", null);
            return ResponseEntity.ok().build();
        } else {
            throw new BadPrivilegesException();
        }
    }

    @GetMapping("/whoAmI")
    public ResponseEntity<?> whoAmI() {
        User requester = getRequestUser();
        trace(requester, "load his details.", null);
        return ResponseEntity.ok(requester);
    }


    @GetMapping("/confirm")
    public ResponseEntity<?> confirmRegistration
            (@RequestParam("token") String token) {
        ActionToken actionToken = getActionToken(token, ActionType.VERIFY_EMAIL);
        User user = actionToken.getUser();
        user.setEnabled(true);
        userService.save(user);
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
        userService.save(requester);
        return jwt;
    }

}
