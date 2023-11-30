package com.bci.users.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users implements Serializable {
    private static final long serialVersionUID = -3203759682534265283L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private ZonedDateTime created;
    private ZonedDateTime lastLogin;
    private boolean isActive;
    private String username;
    private String email;
    private String password;
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phones> phones;
    @ManyToMany( cascade = CascadeType.ALL)
    private List<Roles> roles;
}
