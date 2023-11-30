package com.bci.users.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
    private List<Role> roles;
}
