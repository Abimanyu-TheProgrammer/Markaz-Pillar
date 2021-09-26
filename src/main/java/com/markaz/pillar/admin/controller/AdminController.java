package com.markaz.pillar.admin.controller;

import com.markaz.pillar.admin.service.AdminService;
import com.markaz.pillar.auth.repository.models.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated() and hasAuthority('CRUD_USERS')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/all-users")
    public ResponseEntity<Object> getAllUsers(){
        try {
            return new ResponseEntity<>(adminService.getAllUsers(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Object> getUser(@PathVariable String email)  {
        try {
            return new ResponseEntity<>(adminService.getUser(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile(@RequestHeader("Authorization") String bearerToken)  {
        try {
            return new ResponseEntity<>(adminService.getProfile(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
