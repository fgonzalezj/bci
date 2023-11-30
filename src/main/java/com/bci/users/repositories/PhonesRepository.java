package com.bci.users.repositories;

import com.bci.users.entities.Phones;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhonesRepository extends JpaRepository<Phones, UUID> {}
