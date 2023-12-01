package com.bci.users.controllers;

import com.bci.users.exceptions.NotFoundException;
import com.bci.users.services.UsersService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@RestController
@RequestMapping("/login")
@Validated
public class LoginController {
  private final UsersService usersService;

  public LoginController(UsersService usersService) {
    this.usersService = usersService;
  }

  @GetMapping
  public ResponseEntity<?> retrieveUserByToken(@RequestParam String token)
      throws NotFoundException {
    var response = usersService.findByToken(token);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
