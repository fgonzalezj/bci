package com.bci.users.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.bci.users.auth.JwtUtil;
import com.bci.users.entities.Phones;
import com.bci.users.entities.Roles;
import com.bci.users.entities.Users;
import com.bci.users.exceptions.ConflictException;
import com.bci.users.repositories.PhonesRepository;
import com.bci.users.repositories.UsersRepository;
import com.bci.users.requests.Phone;
import com.bci.users.requests.Role;
import com.bci.users.requests.UserRequest;
import com.bci.users.responses.UserResponse;
import com.bci.users.services.impl.UsersServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Validator;

@ExtendWith({MockitoExtension.class})
class UsersServiceImplTest {
  private static final String PASSWORD_ENCODED =
      "01234$2a$10$17hqjnQ6sHxLig7Nn8crneQWq9IlQcYAwA5VmRRL/2y.hUQemBZJC";
  private static final String PASSWORD = "pwd12345";
  private static final String EMAIL = "john.smith@gmail.com";
  private static final String INVALID_EMAIL = "john.smith@gmail";
  private static final String NAME = "jsmith";
  private static final String ROLE_NAME = "ROLE_ADMIN";
  private static final String PHONE_NUMBER = "5534123456";
  private static final String COUNTRY_CODE = "55";
  private static final String CITY_CODE = "23";
  private static final String SECRET_KEY = "a7ZwvHhJ6759kb3EZS/TKXzCl59Qpz6K5AMxvQlDtnY=";

  private Users user;

  @Autowired BCryptPasswordEncoder passwordEncoder;
  @Autowired private Validator validator;
  @InjectMocks UsersServiceImpl usersService;
  @Mock private UsersRepository usersRepository;
  @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Mock private PhonesRepository phonesRepository;
  @Mock private JwtUtil jwtUtil;

  @BeforeEach
  void setUp() {
    user =
        Users.builder()
            .id(UUID.randomUUID().toString())
            .created(ZonedDateTime.now())
            .lastLogin(ZonedDateTime.now())
            .isActive(Boolean.TRUE)
            .roles(
                List.of(Roles.builder().id(UUID.randomUUID().toString()).name(ROLE_NAME).build()))
            .phones(
                List.of(
                    Phones.builder()
                        .id(UUID.randomUUID().toString())
                        .users(user)
                        .number(PHONE_NUMBER)
                        .countryCode(COUNTRY_CODE)
                        .cityCode(CITY_CODE)
                        .build()))
            .build();
  }

  @Test
  public void createUser_userWithEmailAlreadyExists_throwsException() {
    var accessToken = generateToken(NAME + EMAIL + PASSWORD);
    when(usersRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.of(user));
    var errorMessage =
        assertThrows(ConflictException.class, () -> usersService.createUser(getUserRequest(EMAIL)))
            .getMessage();
    assertEquals(String.format("User with email %s already exists.", EMAIL), errorMessage);
  }

  @Test
  public void createUser_userWithEmailNotExistsButNameAlreadyExists_throwsException() {
    var accessToken = generateToken(NAME + EMAIL + PASSWORD);
    when(usersRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.empty());
    when(usersRepository.findByUsername(eq(NAME))).thenReturn(Optional.of(user));
    var errorMessage =
        assertThrows(ConflictException.class, () -> usersService.createUser(getUserRequest(EMAIL)))
            .getMessage();
    assertEquals(String.format("User with username %s already exists.", NAME), errorMessage);
  }

  @Test
  public void createUser_userWithEmailOrNameNotExists_created() throws ConflictException {
    var accessToken = generateToken(NAME + EMAIL + PASSWORD);
    var userEntity = getUserEntity();
    when(usersRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.empty());
    when(usersRepository.findByUsername(eq(NAME))).thenReturn(Optional.empty());
    when(usersRepository.save(any(Users.class))).thenReturn(userEntity);
    when(jwtUtil.generateToken(any(String.class))).thenReturn(accessToken);
    var responseExpected =
        UserResponse.builder()
            .id(UUID.fromString(userEntity.getId()))
            .created(userEntity.getCreated())
            .lastLogin(userEntity.getLastLogin())
            .isActive(userEntity.isActive())
            .build();
    var response = usersService.createUser(getUserRequest(EMAIL));
    assertEquals(responseExpected, response);
  }

  private static UserRequest getUserRequest(String email) {
    return UserRequest.builder()
        .email(email)
        .name(NAME)
        .password(PASSWORD)
        .phones(getPhonesRequest())
        .roles(getRolesRequest())
        .build();
  }

  private static List<Phone> getPhonesRequest() {
    return List.of(
        Phone.builder().number(PHONE_NUMBER).cityCode(CITY_CODE).countryCode(COUNTRY_CODE).build());
  }

  private static List<Role> getRolesRequest() {
    return List.of(Role.builder().name("ROLE_ADMIN").build());
  }

  private static Users getUserEntity() {
    return Users.builder()
        .id(UUID.randomUUID().toString())
        .created(ZonedDateTime.now())
        .lastLogin(ZonedDateTime.now())
        .email(EMAIL)
        .isActive(Boolean.TRUE)
        .password(PASSWORD_ENCODED)
        .phones(getPhonesEntity())
        .roles(List.of(Roles.builder().id(UUID.randomUUID().toString()).name(ROLE_NAME).build()))
        .build();
  }

  private static List<Phones> getPhonesEntity() {
    return List.of(
        Phones.builder()
            .id(UUID.randomUUID().toString())
            .cityCode(CITY_CODE)
            .countryCode(COUNTRY_CODE)
            .number(PHONE_NUMBER)
            .build());
  }

  public static String generateToken(String subject) {
    byte[] secretBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
    Key secretKey = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());

    return Jwts.builder()
        .setSubject(subject)
        .setExpiration(new Date(System.currentTimeMillis() + Long.valueOf(10800) * 1000))
        .signWith(secretKey)
        .compact();
  }
}
