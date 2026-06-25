package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.enums.Role;
import com.gnanodaya.lms.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByPhoneAndRole(String phone, Role role);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByRole(Role role);

    List<User> findByInstituteId(Long instituteId);

    List<User> findByInstituteIdAndRole(Long instituteId, Role role);
    List<User> findByRole(Role role);

    // ── ADDED ──────────────────────────────────────────────
    List<User> findByStatusAndRole(UserStatus status, Role role);

    List<User> findByStatus(UserStatus status);

}