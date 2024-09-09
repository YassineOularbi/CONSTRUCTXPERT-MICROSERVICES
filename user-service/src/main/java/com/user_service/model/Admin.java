package com.user_service.model;

import com.user_service.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "admin")
public class Admin extends User implements Serializable {

    public Admin(Long id, String username, String password, String email, Role role, String profilePicture) {
        super(id, username, password, email, role, profilePicture);
        this.setRole(Role.ADMIN);
    }

    public Admin() {
        this.setRole(Role.ADMIN);
    }
}
