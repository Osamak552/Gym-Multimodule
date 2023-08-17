package com.epam.repositories;

import com.epam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByUsernameAndPassword(String username,String password);
}
