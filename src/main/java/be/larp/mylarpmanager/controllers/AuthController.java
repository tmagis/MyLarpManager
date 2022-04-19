package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.requests.ChangePasswordRequest;
import be.larp.mylarpmanager.requests.LoginRequest;
import be.larp.mylarpmanager.responses.Errors;
import be.larp.mylarpmanager.responses.Token;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController extends Controller {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        trace(getRequestUser(), "successful login.");
        return ResponseEntity.ok(new Token(jwt));
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> reset(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        User user = getRequestUser();
        if(encoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getNewPasswordConfirmation())) {
                user.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
                userRepository.saveAndFlush(user);
                trace(user, "password change.");
                return ResponseEntity.ok().build();
            }else {
                throw new BadRequestException("The two passwords don't match.");
            }
        }else{
            throw new BadCredentialsException("The current password is not valid.");
        }
    }

    @GetMapping("/whoami")
    public ResponseEntity<?> whoAmI() {
        User user = getRequestUser();
        trace(user, "load details.");
        return ResponseEntity.ok(user);
    }

    @GetMapping("/renew")
    public ResponseEntity<?> renew() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwt = jwtUtils.generateJwtToken(authentication);
        trace(getRequestUser(), "renew JWT Token");
        return ResponseEntity.ok(new Token(jwt));
    }

}
