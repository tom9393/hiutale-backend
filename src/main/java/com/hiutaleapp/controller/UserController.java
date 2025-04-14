package com.hiutaleapp.controller;

import com.hiutaleapp.dto.AuthDTO;
import com.hiutaleapp.dto.UserDTO;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.service.UserService;
import com.hiutaleapp.util.forms.LoginForm;
import com.hiutaleapp.util.forms.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

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
        return userService.createUser(registerForm);
    }

    @PostMapping("/login")
    public AuthDTO loginUser(@RequestBody LoginForm loginForm) {
        return userService.loginUser(loginForm);
    }
}