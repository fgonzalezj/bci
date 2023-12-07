package com.bci.users.controllers;

import com.bci.users.exceptions.ExceptionDetail;
import com.bci.users.exceptions.NotFoundException;
import com.bci.users.responses.LoginResponse;
import com.bci.users.services.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/login")
@Validated
@Api(value = "Service for user's login.", description = "Service for user's login.")
public class LoginController {
  private final UsersService usersService;

  public LoginController(UsersService usersService) {
    this.usersService = usersService;
  }

  @GetMapping
  @ApiOperation(
      value = "Users' login by token.",
      response = LoginResponse.class,
      produces = "application/json")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "User found by token.", response = LoginResponse.class),
        @ApiResponse(
            code = 404,
            message = "User not found by token.",
            response = ExceptionDetail.class)
      })
  public ResponseEntity<?> retrieveUserByToken(@RequestParam String token)
      throws NotFoundException {
    var response = usersService.findByToken(token);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
