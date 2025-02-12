package com.hiutaleapp.dto;

import com.hiutaleapp.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {

    private String token;
    private UserDTO user;

    public AuthDTO(UserDTO userDTO, String token) {
        this.user = userDTO;
        this.token = token;
    }
}
