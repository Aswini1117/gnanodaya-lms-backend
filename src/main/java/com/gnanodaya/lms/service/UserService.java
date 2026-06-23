package com.gnanodaya.lms.service;

import com.gnanodaya.lms.dto.request.CreateUserRequest;
import com.gnanodaya.lms.entity.Institute;
import com.gnanodaya.lms.entity.User;
import com.gnanodaya.lms.enums.Role;
import com.gnanodaya.lms.enums.UserStatus;
import com.gnanodaya.lms.repository.InstituteRepository;
import com.gnanodaya.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InstituteRepository instituteRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already registered");
        }
        Institute institute = null;
        if (request.getInstituteId() != null) {
            institute = instituteRepository.findById(request.getInstituteId())
                    .orElseThrow(() -> new RuntimeException("Institute not found"));
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .status(UserStatus.ACTIVE)
                .institute(institute)
                .build();
        return userRepository.save(user);
    }

    public List<User> getUsersByInstitute(Long instituteId) {
        return userRepository.findByInstituteId(instituteId);
    }

    public List<User> getUsersByInstituteAndRole(Long instituteId, String role) {
        return userRepository.findByInstituteIdAndRole(instituteId, Role.valueOf(role.toUpperCase()));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUserStatus(Long id, String status) {
        User user = getUserById(id);
        user.setStatus(UserStatus.valueOf(status.toUpperCase()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}