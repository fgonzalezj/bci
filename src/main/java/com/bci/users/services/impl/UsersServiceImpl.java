package com.bci.users.services.impl;

import com.bci.users.auth.JwtUtil;
import com.bci.users.entities.Phones;
import com.bci.users.entities.Roles;
import com.bci.users.entities.Users;
import com.bci.users.exceptions.ConflictException;
import com.bci.users.exceptions.NotFoundException;
import com.bci.users.repositories.PhonesRepository;
import com.bci.users.repositories.UsersRepository;
import com.bci.users.requests.UserRequest;
import com.bci.users.responses.LoginResponse;
import com.bci.users.responses.Phone;
import com.bci.users.responses.Role;
import com.bci.users.responses.UserResponse;
import com.bci.users.services.UsersService;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UsersServiceImpl implements UsersService {
  private final UsersRepository usersRepository;
  private final PhonesRepository phonesRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JwtUtil jwtUtil;

  public UsersServiceImpl(
      UsersRepository usersRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      PhonesRepository phonesRepository,
      JwtUtil jwtUtil) {
    this.usersRepository = usersRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.phonesRepository = phonesRepository;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var userOptional = usersRepository.findByUsername(username);
    if (userOptional.isEmpty()) {
      throw new UsernameNotFoundException(
          String.format("User with name %s does not exist,", username));
    }
    var user = userOptional.get();
    List<GrantedAuthority> authorities =
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

    return new User(
        user.getUsername(),
        String.valueOf(user.getPassword()),
        user.isActive(),
        true,
        true,
        true,
        authorities);
  }

  @Override
  public UserResponse createUser(UserRequest userRequest) throws ConflictException {
    if (usersRepository.findByEmail(userRequest.getEmail()).isPresent()) {
      throw new ConflictException(
          String.format("User with email %s already exists.", userRequest.getEmail()));
    }
    if (usersRepository.findByUsername(userRequest.getName()).isPresent()) {
      throw new ConflictException(
          String.format("User with username %s already exists.", userRequest.getName()));
    }
    var accessToken = jwtUtil.generateToken(userRequest.getName() + userRequest.getEmail() + userRequest.getPassword());
    List<Roles> roles =
        userRequest.getRoles().stream()
            .map(
                role -> {
                  return Roles.builder()
                      .id(UUID.randomUUID().toString())
                      .name(role.getName())
                      .build();
                })
            .collect(Collectors.toList());

    var user =
        Users.builder()
            .id(UUID.randomUUID().toString())
            .email(userRequest.getEmail())
            .isActive(Boolean.TRUE)
            .lastLogin(ZonedDateTime.now())
            .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
            .created(ZonedDateTime.now())
            .username(userRequest.getName())
            .token(accessToken)
            .roles(roles)
            .build();

    var userSaved = usersRepository.save(user);

    List<Phones> phones =
        userRequest.getPhones().stream()
            .map(
                phone -> {
                  return Phones.builder()
                      .cityCode(phone.getCityCode())
                      .id(UUID.randomUUID().toString())
                      .countryCode(phone.getCountryCode())
                      .number(phone.getNumber())
                      .users(userSaved)
                      .build();
                })
            .collect(Collectors.toList());
    phonesRepository.saveAll(phones);
    return UserResponse.builder()
        .id(UUID.fromString(userSaved.getId()))
        .created(userSaved.getCreated())
        .lastLogin(userSaved.getLastLogin())
        .isActive(userSaved.isActive())
        .token(userSaved.getToken())
        .build();
  }

  @Override
  public LoginResponse findByToken(String token) throws NotFoundException {
    var optionalUser = usersRepository.findByToken(token);
    var user = optionalUser.orElseThrow(() -> new NotFoundException(
            String.format("User with token %s does not exist.",token)));
    return LoginResponse.builder()
            .id(UUID.fromString(user.getId()))
            .created(user.getCreated())
            .lastLogin(user.getLastLogin())
            .token(user.getToken())
            .isActive(user.isActive())
            .name(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .phones(user.getPhones().stream().map(phone -> {
              return Phone.builder()
                     .countryCode(phone.getCountryCode())
                     .cityCode(phone.getCityCode())
                     .number(phone.getNumber())
                     .build();
            }).collect(Collectors.toList()))
            .build();
  }

}
