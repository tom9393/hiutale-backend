package com.hiutaleapp.service;

import com.hiutaleapp.dto.UserDTO;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

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

    public UserDTO createUser(User user) {
        return mapToDTO(userRepository.save(user));
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