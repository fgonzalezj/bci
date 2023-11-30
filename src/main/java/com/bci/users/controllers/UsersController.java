package com.bci.users.controllers;

import com.bci.users.requests.User;
import com.bci.users.services.UsersService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        log.info("crearUsuario");
        usersService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping
    public ResponseEntity<?> retrieveUsers() {
        log.info("retrieve users");
        var users = usersService.retrieveAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);

    }


}
