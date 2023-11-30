package com.bci.users.services;

import com.bci.users.entities.Phones;
import com.bci.users.entities.Roles;
import com.bci.users.entities.Users;
import com.bci.users.repositories.PhonesRepository;
import com.bci.users.repositories.UsersRepository;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UsersService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final PhonesRepository phonesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersService (UsersRepository usersRepository,
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
            throw new UsernameNotFoundException(String.format("User with name %s does not exist,", username));
        }
        var user = userOptional.get();
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
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

    public void createUser (com.bci.users.requests.User user) {
        List<Roles> roles =  user.getRoles().stream().map(role -> {
            return Roles.builder()
                    .name(role.getName())
                    .build();
        }).collect(Collectors.toList());

        var users = Users.builder()
                .email(user.getEmail())
                .isActive(Boolean.TRUE)
                .lastLogin(ZonedDateTime.now())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .created(ZonedDateTime.now())
                .username(user.getName())
                .roles(roles)
                .build();
        var usersSaved = usersRepository.save(users);
        List<Phones> phones =  user.getPhones().stream().map(phone -> {
            return Phones.builder().cityCode(phone.getCityCode())
                    .countryCode(phone.getCountryCode())
                    .number(phone.getNumber())
                    .users(usersSaved)
                    .build();
        }).collect(Collectors.toList());
        phonesRepository.saveAll(phones);
    }

    public List<com.bci.users.responses.User> retrieveAllUsers () {
        var users = usersRepository.findAll();
        if (users.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        return users.stream().map(user -> {
            var roles = user.getRoles().stream().map(role -> {
                return com.bci.users.responses.Role.builder()
                        .id(role.getId().toString())
                        .name(role.getName())
                        .build();
            }).collect(Collectors.toList());
            log.info(roles);
            var phones = user.getPhones().stream().map(phone -> {
                return com.bci.users.responses.Phone.builder()
                        .number(phone.getNumber())
                        .cityCode(phone.getCityCode())
                        .countryCode(phone.getCountryCode())
                        .build();
            }).collect(Collectors.toList());
            return com.bci.users.responses.User.builder()
                    .name(user.getUsername())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .roles(roles)
                    .phones(phones)
                    .build();

        }).collect(Collectors.toList());

    }
}
