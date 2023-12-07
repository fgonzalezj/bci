package com.bci.users.controllers;

import com.bci.users.exceptions.ConflictException;
import com.bci.users.requests.UserRequest;
import com.bci.users.responses.UserResponse;
import com.bci.users.services.UsersService;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/sign-up")
@Validated
@Api(value = "UsersController", description = "Users management operations.")
public class UsersController {
  private final UsersService usersService;

  public UsersController(UsersService usersService) {
    this.usersService = usersService;
  }

  @PostMapping
  @ApiOperation(value = "Create a new user", response = UserResponse.class)
  public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest)
      throws ConflictException {
    var response = usersService.createUser(userRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}
