package com.placementguru.service;

import com.placementguru.config.JwtTokenProvider;
import com.placementguru.dto.JwtResponse;
import com.placementguru.dto.LoginRequest;
import com.placementguru.dto.SignupRequest;
import com.placementguru.model.User;
import com.placementguru.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public JwtResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        Set<String> roles = new HashSet<>();
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            roles.add("ADMIN");
        } else {
            roles.add("STUDENT");
        }
        user.setRoles(roles);
        
        user.setSkillLevel(request.getSkillLevel() != null ? request.getSkillLevel() : "BEGINNER");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        String token = tokenProvider.generateToken(savedUser.getEmail());

        return new JwtResponse(token, savedUser.getId(), savedUser.getName(), 
                              savedUser.getEmail(), savedUser.getRoles());
    }

    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        String token = tokenProvider.generateToken(user.getEmail());

        return new JwtResponse(token, user.getId(), user.getName(), 
                              user.getEmail(), user.getRoles());
    }
}
