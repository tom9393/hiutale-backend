package com.hiutaleapp.controller;

import com.hiutaleapp.dto.AuthDTO;
import com.hiutaleapp.dto.UserDTO;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.service.UserService;
import com.hiutaleapp.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public UserDTO createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/update/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/register")
    public AuthDTO registerUser(@RequestBody RegisterForm registerForm) {
        User user = new User();
        user.setUsername(registerForm.getUsername());
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        user.setRole("USER");
        try {
            UserDTO userDTO = userService.createUser(user); // Throws DataIntegrityViolationException, CannotCreateTransactionException
            String token = jwtService.generateToken(userService.loadUserById(userDTO.getId()));
            return new AuthDTO(userDTO, token);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateUserException("User with email address" + registerForm.getEmail() + " already exists");
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        }
    }

    @PostMapping("/login")
    public AuthDTO loginUser(@RequestBody LoginForm loginForm) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword())); // Throws AuthenticationException, InternalAuthenticationServiceException
            if (auth.isAuthenticated()) {
                Optional<UserDTO> userDTO = userService.getUserByEmail(loginForm.getEmail()); // Throws DataAccessResourceFailureException
                if (userDTO.isPresent()) { // This should never fail since we've already authenticated above but just to be safe
                    return new AuthDTO(userDTO.get(), jwtService.generateToken(userService.loadUserById(userDTO.get().getId())));
                } else {
                    throw new UsernameNotFoundException("User not found: " + loginForm.getEmail());
                }
            } else {
                throw new AuthenticationCredentialsNotFoundException("Could not verify credentials for user: " + loginForm.getEmail());
            }
        } catch (DataAccessResourceFailureException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        }
    }
}