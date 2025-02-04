package com.hiutaleapp.service;

import com.hiutaleapp.dto.UserDTO;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
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
        user.setUserId(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setPasswordHash(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        return user;
    }
}