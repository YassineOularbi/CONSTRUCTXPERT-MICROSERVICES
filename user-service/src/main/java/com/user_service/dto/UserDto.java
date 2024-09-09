package com.user_service.dto;

import com.user_service.enums.Role;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private String username;
    private String password;
    private String email;
    private Role role;
    private String profilePicture;
}
