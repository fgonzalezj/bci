package com.bci.users.services;

import com.bci.users.requests.UserRequest;
import com.bci.users.responses.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsersService extends UserDetailsService {
    public UserResponse createUser (UserRequest userRequest);
    public List<UserResponse> retrieveAllUsers ();
}
