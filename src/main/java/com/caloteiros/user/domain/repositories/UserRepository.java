package com.caloteiros.user.domain.repositories;

import com.caloteiros.user.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Long findIdByEmail(@Param("email") String email);

    @Query("SELECT u.id FROM User u WHERE u.name = :name")
    Long findIdByName(@Param("name") String name);
}
