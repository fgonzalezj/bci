package com.bci.users.services;

import com.bci.users.exceptions.ConflictException;
import com.bci.users.exceptions.NotFoundException;
import com.bci.users.requests.UserRequest;
import com.bci.users.responses.LoginResponse;
import com.bci.users.responses.UserResponse;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {
  public UserResponse createUser(UserRequest userRequest) throws ConflictException;
  public LoginResponse findByToken(String token) throws NotFoundException;
}
