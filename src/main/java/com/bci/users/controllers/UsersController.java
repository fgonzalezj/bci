package com.bci.users.controllers;

import com.bci.users.requests.UserRequest;
import com.bci.users.services.UsersService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.extern.log4j.Log4j2;

import javax.validation.Valid;

@Log4j2
@RestController
@RequestMapping("/users")
@Validated
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest) {
        log.info("crearUsuario");
        var response = usersService.createUser(userRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<?> retrieveUsers() {
        log.info("retrieve users");
        var response = usersService.retrieveAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
