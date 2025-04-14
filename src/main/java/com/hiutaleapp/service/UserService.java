package com.hiutaleapp.service;

import com.hiutaleapp.dto.AuthDTO;
import com.hiutaleapp.dto.UserDTO;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.repository.UserRepository;
import com.hiutaleapp.util.*;
import com.hiutaleapp.util.errors.DataViolationException;
import com.hiutaleapp.util.errors.DatabaseConnectionException;
import com.hiutaleapp.util.forms.LoginForm;
import com.hiutaleapp.util.forms.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::mapToDTO);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::mapToDTO);
    }

    public AuthDTO createUser(RegisterForm registerForm) {
        User user = new User();
        user.setUsername(registerForm.getUsername());
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        user.setRole("USER");
        try {
            UserDTO userDTO = mapToDTO(userRepository.save(user));
            String token = jwtService.generateToken(loadUserById(userDTO.getId()));
            return new AuthDTO(userDTO, token);
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("User with email address" + registerForm.getEmail() + " already exists");
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        }
    }

    public AuthDTO loginUser(LoginForm loginForm) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword())); // Throws AuthenticationException, InternalAuthenticationServiceException
            if (auth.isAuthenticated()) {
                Optional<UserDTO> userDTO = getUserByEmail(loginForm.getEmail()); // Throws DataAccessResourceFailureException
                if (userDTO.isPresent()) { // This should never fail since we've already authenticated above but just to be safe
                    return new AuthDTO(userDTO.get(), jwtService.generateToken(loadUserById(userDTO.get().getId())));
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

    public UserDTO updateUser(Long id, User user) {
        user.setUserId(id);
        return mapToDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDTO mapToDTO(User user) {
        return new UserDTO(user);
    }

    public User mapToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUserId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with email: %s.", email)));

        return org.springframework.security.core.userdetails.User.builder()
                .username(Long.toString(user.getUserId()))
                .password(user.getPassword())
                .roles(getRoles(user))
                .build();
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with ID: %s.", id)));

        return org.springframework.security.core.userdetails.User.builder()
                .username(Long.toString(user.getUserId()))
                .password(user.getPassword())
                .roles(getRoles(user))
                .build();
    }

    public String[] getRoles(User user) {
        if (user.getRole() == null) {
            return new String[] {"USER"};
        }
        return user.getRole().split(",");
    }
}