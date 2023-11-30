package com.bci.users.services;

import com.bci.users.entities.Phones;
import com.bci.users.entities.Roles;
import com.bci.users.entities.Users;
import com.bci.users.exceptions.ConflictException;
import com.bci.users.repositories.PhonesRepository;
import com.bci.users.repositories.UsersRepository;
import com.bci.users.requests.Phone;
import com.bci.users.requests.Role;
import com.bci.users.requests.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class UsersServiceTest {
    private static final String PASSWORD = "01234$2a$10$17hqjnQ6sHxLig7Nn8crneQWq9IlQcYAwA5VmRRL/2y.hUQemBZJC";
    private static final String EMAIL = "john.smith@gmail.com";
    private static final String NAME = "jsmith";
    private static final String ROLE = "ROLE_ADMIN";
    private static final String PHONE_NUMBER = "5534123456";
    private static final String COUNTRY_CODE = "55";
    private static final String CITY_CODE = "23";

    private Users user;
    @InjectMocks
    UsersService usersService;
    @Mock
    private UsersRepository usersRepository;
    private PhonesRepository phonesRepository;

    @BeforeEach
    void setUp() {
        user = Users
                .builder()
                .id(UUID.randomUUID().toString())
                .created(ZonedDateTime.now())
                .lastLogin(ZonedDateTime.now())
                .isActive(Boolean.TRUE)
                .roles(List.of(
                        Roles
                        .builder()
                        .id(UUID.randomUUID().toString())
                        .name(ROLE)
                        .build()))
                .phones(List.of(
                        Phones
                        .builder()
                                .id(UUID.randomUUID().toString())
                                .users(user)
                                .number(PHONE_NUMBER)
                                .countryCode(COUNTRY_CODE)
                                .cityCode(CITY_CODE)
                                .build()
                ))
                .build();
    }

    @Test
    public void createUser_userWithEmailAlreadyExists_throwsException () {
        when(usersRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.of(user));
        var errorMessage = assertThrows(RuntimeException.class,
                () -> usersService.createUser(getUserRequest())).getMessage();
        assertEquals(String.format("User with email %s already exists.",EMAIL), errorMessage);
    }

    private static UserRequest getUserRequest(){
        return UserRequest
                .builder()
                .email(EMAIL)
                .name(NAME)
                .password(PASSWORD)
                .phones(getPhones())
                .roles(getRoles())
                .build();
    }
    private static List<Phone> getPhones() {
        return List.of(
            Phone.builder()
                .number(PHONE_NUMBER)
                .cityCode(CITY_CODE)
                .countryCode(COUNTRY_CODE)
                .build());
    }

    private static List<Role> getRoles() {
        return List.of(
                Role.builder()
                    .name("ROLE_ADMIN")
                    .build());
    }

}