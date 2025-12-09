package com.rms.jang_rms.modules.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("""
        SELECT u FROM User u
        WHERE LOWER(u.firstName) LIKE %:search%
        OR LOWER(u.lastName) LIKE %:search%
        OR LOWER(u.email) LIKE %:search%
""")
    Page<User> searchUsers(String search, Pageable pageable);

}
