package com.markaz.pillar.auth.controller;

import com.markaz.pillar.auth.admin.model.AuthUserDTO;
import com.markaz.pillar.auth.controller.model.Activity;
import com.markaz.pillar.auth.controller.model.ActivityStatus;
import com.markaz.pillar.auth.controller.model.ActivityType;
import com.markaz.pillar.auth.controller.model.EditProfileRequestDTO;
import com.markaz.pillar.auth.repository.UserActivityRepository;
import com.markaz.pillar.auth.repository.UserRepository;
import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.tools.file.impl.StaticImageStorage;
import com.markaz.pillar.transaction.controller.model.TransactionDTO;
import com.markaz.pillar.transaction.repository.model.UserTransaction;
import com.markaz.pillar.volunteer.admin.controller.model.RegistrationDTO;
import com.markaz.pillar.volunteer.repository.model.VolunteerRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private UserActivityRepository activityRepository;
    private StaticImageStorage fileStorage;

    @Autowired
    public void setActivityRepository(UserActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setFileStorage(StaticImageStorage fileStorage) {
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

    @GetMapping("/activity")
    public Page<Activity> fetchAllUserActivity(@RequestParam(defaultValue = "ALL") ActivityType type,
                                               @RequestParam(required = false) ActivityStatus status,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int n) {
        return activityRepository.fetchAllUserActivity(type, status, PageRequest.of(page, n))
                .map(activity -> {
                    ActivityType activityType = ActivityType.valueOf(activity.getType());
                    if(activityType.equals(ActivityType.VOLUNTEER)) {
                        activity.setData(RegistrationDTO.mapFrom((VolunteerRegistration) activity.getData()));
                    } else if(activityType.equals(ActivityType.TRANSACTION)) {
                        activity.setData(TransactionDTO.mapFrom((UserTransaction) activity.getData()));
                    } else {
                        throw new IllegalStateException("Unsupported function!");
                    }

                    return activity;
                });
    }
}
