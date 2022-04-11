package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.requests.ChangePasswordRequest;
import be.larp.mylarpmanager.requests.ChangeUserDetailsRequest;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/userdetails")
public class UserDetailsController extends Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/changedetails")
    public ResponseEntity<?> reset(@Valid @RequestBody ChangeUserDetailsRequest changeUserDetailsRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setUsername(changeUserDetailsRequest.getUsername());
        user.setEmail(changeUserDetailsRequest.getEmail());
        user.setFirstName(changeUserDetailsRequest.getFirstName());
        user.setLastName(changeUserDetailsRequest.getLastName());
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok(user);
    }


}
