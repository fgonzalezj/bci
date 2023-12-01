package com.bci.users.repositories;

import com.bci.users.entities.Users;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, UUID> {

  Optional<Users> findByUsername(String name);

  Optional<Users> findByEmail(String email);

  Optional<Users> findByToken(String token);
}
