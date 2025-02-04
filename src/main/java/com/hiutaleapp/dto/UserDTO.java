package com.hiutaleapp.dto;

import com.hiutaleapp.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDTO {
    private Long userId;
    private String userName;
    private String password;
    private String email;
    private String role;
    private Date createdAt;
    private Date updatedAt;

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.password = user.getPasswordHash();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}