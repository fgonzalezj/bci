package com.bci.users.repositories;

import com.bci.users.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByUsername(String name);


}
