package com.example.hahn_internship.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hahn_internship.model.User;

/**
 * Repository interface for managing {@link User} entities.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email of the user to find
     * @return an Optional containing the user if found, otherwise empty
     */
    Optional<User> findByEmail(String email);
}
