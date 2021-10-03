package com.markaz.pillar.auth.service;

import com.markaz.pillar.auth.repository.RoleRepository;
import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthUser register(@Valid AuthUser data){
        Optional<AuthUser> existingUser = userRepository.findByEmail(data.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email is already associated with a user");
        }

        data.setRole(roleRepository.findByName("MEMBER"));
        data.setPassword(passwordEncoder.encode(data.getPassword()));

        return userRepository.saveAndFlush(data);
    }
}


