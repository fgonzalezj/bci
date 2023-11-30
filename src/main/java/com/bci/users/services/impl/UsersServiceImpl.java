package com.bci.users.services.impl;

import com.bci.users.entities.Phones;
import com.bci.users.entities.Roles;
import com.bci.users.entities.Users;
import com.bci.users.exceptions.ConflictException;
import com.bci.users.repositories.PhonesRepository;
import com.bci.users.repositories.UsersRepository;
import com.bci.users.requests.UserRequest;
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

  public UsersServiceImpl(
      UsersRepository usersRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      PhonesRepository phonesRepository) {
    this.usersRepository = usersRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.phonesRepository = phonesRepository;
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
  public UserResponse createUser(UserRequest userRequest) {
    if (usersRepository.findByEmail(userRequest.getEmail()).isPresent()) {
      throw new ConflictException(
          String.format("User with email %s already exists.", userRequest.getEmail()));
    }
    if (usersRepository.findByUsername(userRequest.getName()).isPresent()) {
      throw new ConflictException(
          String.format("User with username %s already exists.", userRequest.getName()));
    }
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
        .build();
  }

  public List<UserResponse> retrieveAllUsers() {
    var users = usersRepository.findAll();
    if (users.isEmpty()) {
      return Collections.EMPTY_LIST;
    }

    return users.stream()
        .map(
            user -> {
              var roles =
                  user.getRoles().stream()
                      .map(
                          role -> {
                            return Role.builder()
                                .id(role.getId().toString())
                                .name(role.getName())
                                .build();
                          })
                      .collect(Collectors.toList());
              log.info(roles);
              var phones =
                  user.getPhones().stream()
                      .map(
                          phone -> {
                            return Phone.builder()
                                .number(phone.getNumber())
                                .cityCode(phone.getCityCode())
                                .countryCode(phone.getCountryCode())
                                .build();
                          })
                      .collect(Collectors.toList());
              return UserResponse.builder()
                  .id(UUID.fromString(user.getId()))
                  .created(user.getCreated())
                  .lastLogin(user.getLastLogin())
                  .isActive(user.isActive())
                  // .name(user.getUsername())
                  // .email(user.getEmail())
                  // .password(user.getPassword())
                  // .roles(roles)
                  // .phones(phones)
                  .build();
            })
        .collect(Collectors.toList());
  }
}
