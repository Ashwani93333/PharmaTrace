package com.pharmaTrace.repository;


import com.pharmaTrace.entity.Role;
import com.pharmaTrace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndIsActiveTrue(Long id);

    List<User> findByRole(Role role);

    Page<User> findByRole(Role role, Pageable pageable);

    Page<User> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = ?1")
    Long countByRole(Role role);

    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.createdAt DESC")
    Page<User> findAllActiveUsers(Pageable pageable);

    boolean existsByEmail(String email);
}