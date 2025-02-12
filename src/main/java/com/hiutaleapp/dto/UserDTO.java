package com.hiutaleapp.dto;

import com.hiutaleapp.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String email;

    public UserDTO(User user) {
        this.id = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}