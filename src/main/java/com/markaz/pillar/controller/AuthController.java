package com.markaz.pillar.controller;

import com.markaz.pillar.models.User;
import com.markaz.pillar.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")

public class AuthController {
    @Autowired
    private AuthService authService;

    @PreAuthorize("permitAll()")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Map> register(@Valid @RequestBody User user){
        Map<String, String> message = new HashMap<>();
        try {
            authService.register(user);
            message.put("message", "User Successfully Registered");
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (DataIntegrityViolationException ex) {
            message.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        } catch (Exception ex) {
            message.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
