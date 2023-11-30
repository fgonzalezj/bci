package com.bci.users.services;

import com.bci.users.requests.UserRequest;
import com.bci.users.responses.UserResponse;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {
  public UserResponse createUser(UserRequest userRequest);

  public List<UserResponse> retrieveAllUsers();
}
