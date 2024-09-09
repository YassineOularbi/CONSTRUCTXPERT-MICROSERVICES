package com.user_service.model;

import com.user_service.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Entity
@Getter
@Setter
@Table(name = "client")
public class Client extends User implements Serializable {

    public Client(Long id, String username, String password, String email, Role role, String profilePicture) {
        super(id, username, password, email, role, profilePicture);
        this.setRole(Role.CLIENT);
    }

    public Client() {
        this.setRole(Role.CLIENT);
    }
}
