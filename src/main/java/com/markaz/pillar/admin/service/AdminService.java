package com.markaz.pillar.admin.service;

import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public List<AuthUser> getAllUsers(){
        return userRepository.findAll();
    }

    public AuthUser getUser(String email) throws Exception {
        return userRepository.findByEmail(email).orElseThrow(() -> new Exception(String.format("User %s does not exist", email)));
    }

    public AuthUser getProfile() throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        return getUser(email);
    }
}
