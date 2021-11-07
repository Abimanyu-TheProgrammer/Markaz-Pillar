package com.markaz.pillar.auth.controller;

import com.markaz.pillar.auth.admin.model.AuthUserDTO;
import com.markaz.pillar.auth.controller.model.EditProfileRequestDTO;
import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.tools.file.impl.StaticFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;

@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
public class UserController {
    private UserRepository repository;
    private StaticFileStorage fileStorage;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setFileStorage(StaticFileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @GetMapping
    public AuthUserDTO getProfile(Principal principal) {
        return AuthUserDTO.mapFrom(
                repository.findByEmail(principal.getName())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid User!"))
        );
    }

    @PostMapping
    public AuthUserDTO editProfile(@RequestPart @Valid EditProfileRequestDTO detail,
                                   @RequestPart(required = false) MultipartFile profile,
                                   Principal principal) throws IOException {
        AuthUser user = repository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid User!"));

        user.setFullName(detail.getFullName() == null ? user.getFullName() : detail.getFullName());
        user.setPhoneNum(detail.getPhoneNum() == null ? user.getPhoneNum() : detail.getPhoneNum());
        user.setFullName(detail.getFullName() == null ? user.getFullName() : detail.getFullName());
        user.setAddress(detail.getAddress() == null ? user.getAddress() : detail.getAddress());
        user.setDescription(detail.getDescription() == null ? user.getDescription() : detail.getDescription());

        if(!profile.isEmpty()) {
            String url = fileStorage.saveFile(profile, Paths.get("user", user.getUsername()));
            user.setProfileURL(url);
        }

        return AuthUserDTO.mapFrom(repository.save(user));
    }
}
