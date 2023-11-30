package com.bci.users.repositories;

import com.bci.users.entities.Phones;
import com.bci.users.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PhonesRepository extends JpaRepository<Phones, UUID> {

}
