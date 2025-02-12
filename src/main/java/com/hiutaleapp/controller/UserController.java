package com.hiutaleapp.controller;

import com.hiutaleapp.dto.AuthDTO;
import com.hiutaleapp.dto.UserDTO;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.service.UserService;
import com.hiutaleapp.util.JwtService;
import com.hiutaleapp.util.LoginForm;
import com.hiutaleapp.util.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public AuthDTO registerUser(@RequestBody RegisterForm registerForm) throws IOException {
        User user = new User();
        user.setUsername(registerForm.getUsername());
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        user.setRole("USER");
        UserDTO userDTO = userService.createUser(user);
        if (userDTO != null) {
            String token = jwtService.generateToken(userService.loadUserById(userDTO.getId()));
            return new AuthDTO(userDTO, token);
        } else {
            throw new IOException("Failed to create user: " + registerForm.getEmail());
        }
    }

    @PostMapping("/authenticate")
    public AuthDTO authenticateUser(@RequestBody LoginForm loginForm) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
        if (auth.isAuthenticated()) {
            Optional<UserDTO> userDTO = userService.getUserByEmail(loginForm.getEmail());
            if (userDTO.isPresent()) {
                return new AuthDTO(userDTO.get(), jwtService.generateToken(userService.loadUserById(userDTO.get().getId())));
            } else {
                throw new UsernameNotFoundException("User not found: " + loginForm.getEmail());
            }
        } else {
            throw new AuthenticationCredentialsNotFoundException("Could not verify credentials for user: " + loginForm.getEmail());
        }
    }
}